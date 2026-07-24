package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.ProviderReservationActionRequest;
import com.smartrwanda.tourism.dto.ProviderReservationResponse;
import com.smartrwanda.tourism.dto.ProviderReservationStatistics;
import com.smartrwanda.tourism.entity.Reservation;
import com.smartrwanda.tourism.entity.ReservationStatus;
import com.smartrwanda.tourism.exception.InvalidReservationActionException;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ProviderReservationServiceImpl implements ProviderReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProviderReservationResponse> getReservationsForProvider(Long providerId,
                                                                        ReservationStatus status,
                                                                        Pageable pageable) {
        Page<Reservation> reservations = (status == null)
                ? reservationRepository.findByProviderId(providerId, pageable)
                : reservationRepository.findByProviderIdAndStatus(providerId, status, pageable);

        return reservations.map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderReservationResponse getReservationById(Long providerId, Long reservationId) {
        return toResponse(findOwnedReservation(providerId, reservationId));
    }

    @Override
    public ProviderReservationResponse acceptReservation(Long providerId,
                                                         Long reservationId,
                                                         ProviderReservationActionRequest request) {
        Reservation reservation = findOwnedReservation(providerId, reservationId);
        requireStatus(reservation, ReservationStatus.PENDING, "accepted");

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setConfirmedAt(LocalDateTime.now());

        return toResponse(reservationRepository.save(reservation));
    }

    @Override
    public ProviderReservationResponse rejectReservation(Long providerId,
                                                         Long reservationId,
                                                         ProviderReservationActionRequest request) {
        Reservation reservation = findOwnedReservation(providerId, reservationId);
        requireStatus(reservation, ReservationStatus.PENDING, "rejected");
        requireReason(request, "A reason is required to reject a reservation.");

        reservation.setStatus(ReservationStatus.REJECTED);
        reservation.setRejectedAt(LocalDateTime.now());
        reservation.setRejectionReason(request.getReason());

        return toResponse(reservationRepository.save(reservation));
    }

    @Override
    public ProviderReservationResponse completeReservation(Long providerId, Long reservationId) {
        Reservation reservation = findOwnedReservation(providerId, reservationId);
        requireStatus(reservation, ReservationStatus.CONFIRMED, "marked as completed");

        reservation.setStatus(ReservationStatus.COMPLETED);
        reservation.setCompletedAt(LocalDateTime.now());

        return toResponse(reservationRepository.save(reservation));
    }

    @Override
    public ProviderReservationResponse cancelReservation(Long providerId,
                                                         Long reservationId,
                                                         ProviderReservationActionRequest request) {
        Reservation reservation = findOwnedReservation(providerId, reservationId);
        requireStatus(reservation, ReservationStatus.CONFIRMED, "cancelled");
        requireReason(request, "A reason is required to cancel a reservation.");

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledAt(LocalDateTime.now());
        reservation.setCancellationReason(request.getReason());

        return toResponse(reservationRepository.save(reservation));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProviderReservationResponse> getBookingHistory(Long providerId, Pageable pageable) {
        return reservationRepository.findByProviderId(providerId, pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderReservationStatistics getProviderReservationStatistics(Long providerId) {
        long pending = reservationRepository.countByProviderIdAndStatus(providerId, ReservationStatus.PENDING);
        long confirmed = reservationRepository.countByProviderIdAndStatus(providerId, ReservationStatus.CONFIRMED);
        long completed = reservationRepository.countByProviderIdAndStatus(providerId, ReservationStatus.COMPLETED);
        long cancelled = reservationRepository.countByProviderIdAndStatus(providerId, ReservationStatus.CANCELLED);
        long rejected = reservationRepository.countByProviderIdAndStatus(providerId, ReservationStatus.REJECTED);
        long total = reservationRepository.countByProviderId(providerId);

        Double revenue = reservationRepository.sumTotalPriceByProviderIdAndStatus(
                providerId, ReservationStatus.COMPLETED);

        return ProviderReservationStatistics.builder()
                .providerId(providerId)
                .totalReservations(total)
                .pendingCount(pending)
                .confirmedCount(confirmed)
                .completedCount(completed)
                .cancelledCount(cancelled)
                .rejectedCount(rejected)
                .totalRevenue(revenue)
                .currency("RWF")
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public long getPendingReservationCount(Long providerId) {
        return reservationRepository.countByProviderIdAndStatus(providerId, ReservationStatus.PENDING);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProviderReservationResponse> searchReservations(Long providerId,
                                                                String keyword,
                                                                ReservationStatus status,
                                                                Pageable pageable) {
        return reservationRepository.searchByProviderId(providerId, keyword, status, pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public String exportReservationsAsCsv(Long providerId, ReservationStatus status) {
        java.util.List<Reservation> reservations = (status == null)
                ? reservationRepository.findByProviderId(providerId)
                : reservationRepository.findByProviderIdAndStatus(providerId, status);

        StringBuilder csv = new StringBuilder();
        csv.append("Confirmation Code,Guest Name,Guest Email,Guest Phone,Status,")
                .append("Reservation Date,Total Price,Currency,Special Requests\n");

        for (Reservation r : reservations) {
            csv.append(csvEscape(r.getConfirmationCode())).append(',')
                    .append(csvEscape(r.getGuestName())).append(',')
                    .append(csvEscape(r.getGuestEmail())).append(',')
                    .append(csvEscape(r.getGuestPhone())).append(',')
                    .append(csvEscape(r.getStatus() == null ? "" : r.getStatus().name())).append(',')
                    .append(csvEscape(r.getReservationDate() == null ? "" : r.getReservationDate().toString())).append(',')
                    .append(r.getTotalPrice() == null ? "" : r.getTotalPrice()).append(',')
                    .append(csvEscape(r.getCurrency())).append(',')
                    .append(csvEscape(r.getSpecialRequests())).append('\n');
        }

        return csv.toString();
    }

    private String csvEscape(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private Reservation findOwnedReservation(Long providerId, Long reservationId) {
        return reservationRepository.findByIdAndProviderId(reservationId, providerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reservation " + reservationId + " not found for provider " + providerId));
    }

    private void requireStatus(Reservation reservation, ReservationStatus expected, String actionDescription) {
        if (reservation.getStatus() != expected) {
            throw new InvalidReservationActionException(
                    "Reservation must be " + expected + " to be " + actionDescription +
                            ", but it is currently " + reservation.getStatus());
        }
    }

    private void requireReason(ProviderReservationActionRequest request, String message) {
        if (request == null || request.getReason() == null || request.getReason().isBlank()) {
            throw new InvalidReservationActionException(message);
        }
    }

    private ProviderReservationResponse toResponse(Reservation r) {
        return ProviderReservationResponse.builder()
                .id(r.getId())
                .confirmationCode(r.getConfirmationCode())
                .status(r.getStatus())
                .guestName(r.getGuestName())
                .guestEmail(r.getGuestEmail())
                .guestPhone(r.getGuestPhone())
                .reservationDate(r.getReservationDate())
                .specialRequests(r.getSpecialRequests())
                .totalPrice(r.getTotalPrice())
                .currency(r.getCurrency())
                .checkInDate(r.getCheckInDate())
                .checkOutDate(r.getCheckOutDate())
                .numberOfGuests(r.getNumberOfGuests())
                .roomType(r.getRoomType())
                .pickupDate(r.getPickupDate())
                .returnDate(r.getReturnDate())
                .vehicleType(r.getVehicleType())
                .tourDate(r.getTourDate())
                .tourPackage(r.getTourPackage())
                .numberOfPeople(r.getNumberOfPeople())
                .visitDate(r.getVisitDate())
                .numberOfVisitors(r.getNumberOfVisitors())
                .confirmedAt(r.getConfirmedAt())
                .completedAt(r.getCompletedAt())
                .rejectedAt(r.getRejectedAt())
                .rejectionReason(r.getRejectionReason())
                .cancelledAt(r.getCancelledAt())
                .cancellationReason(r.getCancellationReason())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}