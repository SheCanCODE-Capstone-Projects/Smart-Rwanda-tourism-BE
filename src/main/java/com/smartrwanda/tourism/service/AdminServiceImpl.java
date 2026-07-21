package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.AdminAttractionUpdateRequest;
import com.smartrwanda.tourism.dto.AdminAttractionResponse;
import com.smartrwanda.tourism.dto.AdminProviderResponse;
import com.smartrwanda.tourism.dto.AdminUserResponse;
import com.smartrwanda.tourism.dto.DashboardStatistics;
import com.smartrwanda.tourism.entity.DiscoverItem;
import com.smartrwanda.tourism.entity.Provider;
import com.smartrwanda.tourism.entity.ReservationStatus;
import com.smartrwanda.tourism.entity.User;
import com.smartrwanda.tourism.entity.VerificationStatus;
import com.smartrwanda.tourism.exception.BadRequestException;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.repository.DiscoverRepository;
import com.smartrwanda.tourism.repository.ProviderRepository;
import com.smartrwanda.tourism.repository.ReservationRepository;
import com.smartrwanda.tourism.repository.ReviewRepository;
import com.smartrwanda.tourism.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;
    private final ReservationRepository reservationRepository;
    private final DiscoverRepository discoverRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public DashboardStatistics getDashboardStatistics() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.findByIsActive(true).size();
        long totalProviders = providerRepository.count();
        long verifiedProviders = providerRepository.countByVerificationStatus(VerificationStatus.VERIFIED);
        long pendingProviders = providerRepository.countByVerificationStatus(VerificationStatus.PENDING);
        long totalAttractions = discoverRepository.count();
        long totalBookings = reservationRepository.count();
        long pendingBookings = reservationRepository.countByStatus(ReservationStatus.PENDING);
        long confirmedBookings = reservationRepository.countByStatus(ReservationStatus.CONFIRMED);
        long completedBookings = reservationRepository.countByStatus(ReservationStatus.COMPLETED);
        long totalReviews = reviewRepository.count();
        Double averageRating = reviewRepository.findAverageRating();

        LocalDate today = LocalDate.now();
        long todayBookings = reservationRepository.countByCreatedAtDate(today);
        long thisMonthBookings = reservationRepository.countByCreatedAtMonth(today.getMonthValue(), today.getYear());

        return DashboardStatistics.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .totalProviders(totalProviders)
                .verifiedProviders(verifiedProviders)
                .pendingProviders(pendingProviders)
                .totalAttractions(totalAttractions)
                .totalBookings(totalBookings)
                .pendingBookings(pendingBookings)
                .confirmedBookings(confirmedBookings)
                .completedBookings(completedBookings)
                .totalReviews(totalReviews)
                .averageRating(averageRating != null ? averageRating : 0.0)
                .todayBookings(todayBookings)
                .thisMonthBookings(thisMonthBookings)
                .build();
    }

    @Override
    public List<AdminUserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToAdminUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AdminUserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToAdminUserResponse(user);
    }

    @Override
    @Transactional
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setIsActive(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public List<AdminProviderResponse> getAllProviders() {
        return providerRepository.findAll().stream()
                .map(this::mapToAdminProviderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AdminProviderResponse getProviderById(Long providerId) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        return mapToAdminProviderResponse(provider);
    }

    @Override
    @Transactional
    public void verifyProvider(Long providerId) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));

        if (provider.getVerificationStatus() == VerificationStatus.VERIFIED) {
            throw new BadRequestException("Provider is already verified");
        }

        provider.setVerificationStatus(VerificationStatus.VERIFIED);
        providerRepository.save(provider);
    }

    @Override
    @Transactional
    public void rejectProvider(Long providerId, String reason) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));

        if (provider.getVerificationStatus() == VerificationStatus.REJECTED) {
            throw new BadRequestException("Provider is already rejected");
        }

        provider.setVerificationStatus(VerificationStatus.REJECTED);
        providerRepository.save(provider);
    }

    @Override
    @Transactional
    public void deleteProvider(Long providerId) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        providerRepository.delete(provider);
    }

    @Override
    public List<AdminAttractionResponse> getAllAttractions() {
        return discoverRepository.findAll().stream()
                .map(this::mapToAdminAttractionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AdminAttractionResponse getAttractionById(Long attractionId) {
        DiscoverItem attraction = discoverRepository.findById(attractionId)
                .orElseThrow(() -> new ResourceNotFoundException("Attraction not found"));
        return mapToAdminAttractionResponse(attraction);
    }

    @Override
    @Transactional
    public AdminAttractionResponse updateAttraction(Long attractionId, AdminAttractionUpdateRequest request) {
        DiscoverItem attraction = discoverRepository.findById(attractionId)
                .orElseThrow(() -> new ResourceNotFoundException("Attraction not found"));

        attraction.setName(request.getName());
        attraction.setDescription(request.getDescription());
        attraction.setMainCategory(request.getMainCategory());
        attraction.setSubCategory(request.getSubCategory());
        attraction.setLocation(request.getLocation());
        attraction.setLatitude(request.getLatitude());
        attraction.setLongitude(request.getLongitude());
        attraction.setOpeningHours(request.getOpeningHours());
        attraction.setPrice(request.getPrice());
        attraction.setContactInfo(request.getContactInfo());
        attraction.setWebsite(request.getWebsite());
        attraction.setIsActive(request.getIsActive());
        attraction.setFeatured(request.getFeatured());

        DiscoverItem updated = discoverRepository.save(attraction);
        return mapToAdminAttractionResponse(updated);
    }

    @Override
    @Transactional
    public void deleteAttraction(Long attractionId) {
        DiscoverItem attraction = discoverRepository.findById(attractionId)
                .orElseThrow(() -> new ResourceNotFoundException("Attraction not found"));
        discoverRepository.delete(attraction);
    }

    private AdminUserResponse mapToAdminUserResponse(User user) {
        Long totalBookings = reservationRepository.countByUserId(user.getId());
        Long totalReviews = reviewRepository.countByUserId(user.getId());

        return AdminUserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .phoneNumber(user.getPhoneNumber())
                .profilePicture(user.getProfilePicture())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .totalBookings(totalBookings)
                .totalReviews(totalReviews)
                .build();
    }

    private AdminProviderResponse mapToAdminProviderResponse(Provider provider) {
        Long totalBookings = reservationRepository.countByProviderId(provider.getId());

        // ✅ REMOVED: int totalServices = provider.getServices() != null ? provider.getServices().size() : 0;
        // Using ProviderService to get service count instead

        return AdminProviderResponse.builder()
                .id(provider.getId())
                .businessName(provider.getBusinessName())
                .description(provider.getDescription())
                .category(provider.getCategory())
                .contactEmail(provider.getContactEmail())
                .contactPhone(provider.getContactPhone())
                .location(provider.getLocation())
                .website(provider.getWebsite())
                .verificationStatus(provider.getVerificationStatus())
                .logoUrl(provider.getLogoUrl())
                .coverImageUrl(provider.getCoverImageUrl())
                .averageRating(provider.getAverageRating())
                .totalReviews(provider.getTotalReviews())
                .isActive(provider.getIsActive())
                .userId(provider.getUser().getId())
                .ownerName(provider.getUser().getFirstName() + " " + provider.getUser().getLastName())
                .ownerEmail(provider.getUser().getEmail())
                .createdAt(provider.getCreatedAt())
                .updatedAt(provider.getUpdatedAt())
                .totalBookings(totalBookings)
                .totalServices(0L)  // ✅ Set to 0 or fetch from ProviderService
                .build();
    }

    private AdminAttractionResponse mapToAdminAttractionResponse(DiscoverItem attraction) {
        return AdminAttractionResponse.builder()
                .id(attraction.getId())
                .name(attraction.getName())
                .description(attraction.getDescription())
                .mainCategory(attraction.getMainCategory())
                .subCategory(attraction.getSubCategory())
                .location(attraction.getLocation())
                .latitude(attraction.getLatitude())
                .longitude(attraction.getLongitude())
                .images(attraction.getImages())
                .openingHours(attraction.getOpeningHours())
                .price(attraction.getPrice())
                .contactInfo(attraction.getContactInfo())
                .website(attraction.getWebsite())
                .averageRating(attraction.getAverageRating())
                .totalReviews(attraction.getTotalReviews())
                .isActive(attraction.getIsActive())
                .featured(attraction.getFeatured())
                .createdAt(attraction.getCreatedAt())
                .updatedAt(attraction.getUpdatedAt())
                .build();
    }
}