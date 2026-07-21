package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.ReservationRequest;
import com.smartrwanda.tourism.dto.ReservationResponse;
import com.smartrwanda.tourism.entity.*;
import com.smartrwanda.tourism.exception.BadRequestException;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserReservationServiceImpl implements UserReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;

    private static final String CONFIRMATION_PREFIX = "SRT";

    @Override
    @Transactional
    public ReservationResponse createReservation(ReservationRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Provider provider = providerRepository.findById(request.getProviderId())
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));

        if (provider.getVerificationStatus() != VerificationStatus.VERIFIED) {
            throw new BadRequestException("Provider is not verified");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setProvider(provider);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setGuestName(user.getFirstName() + " " + user.getLastName());
        reservation.setGuestEmail(user.getEmail());
        reservation.setGuestPhone(user.getPhoneNumber());
        reservation.setSpecialRequests(request.getSpecialRequests());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setConfirmationCode(generateConfirmationCode());
        reservation.setTotalPrice(request.getTotalPrice());
        reservation.setCurrency(request.getCurrency());

        ProviderCategory category = provider.getCategory();

        switch (category) {
           
            case HOTEL:
            case MOTEL:
            case APARTMENT:
                validateStayBooking(request);
                reservation.setCheckInDate(request.getCheckInDate());
                reservation.setCheckOutDate(request.getCheckOutDate());
                reservation.setNumberOfGuests(request.getNumberOfGuests());
                reservation.setRoomType(request.getRoomType());
                break;

            case CAR_RENTAL:
                validateCarRentalBooking(request);
                reservation.setPickupDate(request.getPickupDate());
                reservation.setReturnDate(request.getReturnDate());
                reservation.setVehicleType(request.getVehicleType());
                break;


            case TOUR_AGENCY:
                validateTourBooking(request);
                reservation.setTourDate(request.getTourDate());
                reservation.setTourPackage(request.getTourPackage());
                reservation.setNumberOfPeople(request.getNumberOfPeople());
                break;


            case ATTRACTION:
                validateAttractionBooking(request);
                reservation.setVisitDate(request.getVisitDate());
                reservation.setNumberOfVisitors(request.getNumberOfVisitors());
                break;


            case RESTAURANT:
                validateRestaurantBooking(request);
                reservation.setCheckInDate(request.getCheckInDate());
                reservation.setCheckOutDate(request.getCheckOutDate());
                reservation.setNumberOfGuests(request.getNumberOfGuests());
                break;


            case TOUR_GUIDE:
                validateTourGuideBooking(request);
                reservation.setTourDate(request.getTourDate());
                reservation.setNumberOfPeople(request.getNumberOfPeople());
                break;

            default:
                throw new BadRequestException("Unsupported provider category: " + category);
        }

        Reservation saved = reservationRepository.save(reservation);
        return mapToResponse(saved);
    }


    private void validateStayBooking(ReservationRequest request) {
        if (request.getCheckInDate() == null || request.getCheckOutDate() == null) {
            throw new BadRequestException("Check-in and check-out dates are required for accommodation");
        }
        if (request.getCheckOutDate().isBefore(request.getCheckInDate())) {
            throw new BadRequestException("Check-out date must be after check-in date");
        }
        if (request.getNumberOfGuests() == null || request.getNumberOfGuests() < 1) {
            throw new BadRequestException("At least one guest is required");
        }
    }

    private void validateCarRentalBooking(ReservationRequest request) {
        if (request.getPickupDate() == null || request.getReturnDate() == null) {
            throw new BadRequestException("Pickup and return dates are required for car rental");
        }
        if (request.getReturnDate().isBefore(request.getPickupDate())) {
            throw new BadRequestException("Return date must be after pickup date");
        }
        if (request.getVehicleType() == null || request.getVehicleType().isEmpty()) {
            throw new BadRequestException("Vehicle type is required");
        }
    }

    private void validateTourBooking(ReservationRequest request) {
        if (request.getTourDate() == null) {
            throw new BadRequestException("Tour date is required");
        }
        if (request.getNumberOfPeople() == null || request.getNumberOfPeople() < 1) {
            throw new BadRequestException("At least one person is required");
        }
    }

    private void validateAttractionBooking(ReservationRequest request) {
        if (request.getVisitDate() == null) {
            throw new BadRequestException("Visit date is required");
        }
        if (request.getNumberOfVisitors() == null || request.getNumberOfVisitors() < 1) {
            throw new BadRequestException("At least one visitor is required");
        }
    }

    private void validateRestaurantBooking(ReservationRequest request) {
        if (request.getCheckInDate() == null) {
            throw new BadRequestException("Reservation date and time are required for restaurants");
        }
        if (request.getNumberOfGuests() == null || request.getNumberOfGuests() < 1) {
            throw new BadRequestException("At least one guest is required");
        }
    }

    private void validateTourGuideBooking(ReservationRequest request) {
        if (request.getTourDate() == null) {
            throw new BadRequestException("Tour date is required");
        }
        if (request.getNumberOfPeople() == null || request.getNumberOfPeople() < 1) {
            throw new BadRequestException("At least one person is required");
        }
    }



    @Override
    public List<ReservationResponse> getUserReservations(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        List<Reservation> reservations = reservationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return reservations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationResponse getReservationById(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new BadRequestException("You are not authorized to view this reservation");
        }

        return mapToResponse(reservation);
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId, Long userId, String reason) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new BadRequestException("You are not authorized to cancel this reservation");
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new BadRequestException("Reservation is already cancelled");
        }

        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            throw new BadRequestException("Cannot cancel a confirmed reservation. Please contact the provider directly.");
        }

        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel a completed reservation");
        }

        if (reservation.getStatus() == ReservationStatus.REJECTED) {
            throw new BadRequestException("Reservation was already rejected by the provider");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancellationReason(reason);
        reservation.setCancelledAt(LocalDateTime.now());

        reservationRepository.save(reservation);
    }

    @Override
    public List<ReservationResponse> getUserReservationsByStatus(Long userId, ReservationStatus status) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        List<Reservation> reservations = reservationRepository.findByUserIdAndStatus(userId, status);
        return reservations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public long countReservationsByStatus(Long userId, ReservationStatus status) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        return reservationRepository.countByUserIdAndStatus(userId, status);
    }



    private String generateConfirmationCode() {
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return CONFIRMATION_PREFIX + "-" + uuid;
    }

    private ReservationResponse mapToResponse(Reservation reservation) {
        ReservationResponse response = ReservationResponse.builder()
                .id(reservation.getId())
                .userId(reservation.getUser().getId())
                .guestName(reservation.getGuestName())
                .guestEmail(reservation.getGuestEmail())
                .guestPhone(reservation.getGuestPhone())
                .providerId(reservation.getProvider().getId())
                .providerName(reservation.getProvider().getBusinessName())
                .providerLocation(reservation.getProvider().getLocation())
                .providerCategory(reservation.getProvider().getCategory())
                .specialRequests(reservation.getSpecialRequests())
                .status(reservation.getStatus())
                .confirmationCode(reservation.getConfirmationCode())
                .totalPrice(reservation.getTotalPrice())
                .currency(reservation.getCurrency())
                .cancellationReason(reservation.getCancellationReason())
                .cancelledAt(reservation.getCancelledAt())
                .confirmedAt(reservation.getConfirmedAt())
                .completedAt(reservation.getCompletedAt())
                .rejectedAt(reservation.getRejectedAt())
                .rejectionReason(reservation.getRejectionReason())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .build();

        ProviderCategory category = reservation.getProvider().getCategory();

        switch (category) {
            case HOTEL:
            case MOTEL:
            case APARTMENT:
                response.setCheckInDate(reservation.getCheckInDate());
                response.setCheckOutDate(reservation.getCheckOutDate());
                response.setNumberOfGuests(reservation.getNumberOfGuests());
                response.setRoomType(reservation.getRoomType());
                break;

            case CAR_RENTAL:
                response.setPickupDate(reservation.getPickupDate());
                response.setReturnDate(reservation.getReturnDate());
                response.setVehicleType(reservation.getVehicleType());
                break;

            case TOUR_AGENCY:
                response.setTourDate(reservation.getTourDate());
                response.setTourPackage(reservation.getTourPackage());
                response.setNumberOfPeople(reservation.getNumberOfPeople());
                break;

            case ATTRACTION:
                response.setVisitDate(reservation.getVisitDate());
                response.setNumberOfVisitors(reservation.getNumberOfVisitors());
                break;

            case RESTAURANT:
                response.setCheckInDate(reservation.getCheckInDate());
                response.setNumberOfGuests(reservation.getNumberOfGuests());
                break;

            case TOUR_GUIDE:
                response.setTourDate(reservation.getTourDate());
                response.setNumberOfPeople(reservation.getNumberOfPeople());
                break;
        }

        return response;
    }
}
