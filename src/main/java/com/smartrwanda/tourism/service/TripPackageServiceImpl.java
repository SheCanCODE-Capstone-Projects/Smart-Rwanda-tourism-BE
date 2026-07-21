package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.request.MatchTripRequest;
import com.smartrwanda.tourism.dto.request.TripPackageRequest;
import com.smartrwanda.tourism.dto.response.TripPackageResponse;
import com.smartrwanda.tourism.entity.TripPackage;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.repository.TripPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripPackageServiceImpl implements TripPackageService {

    private final TripPackageRepository tripPackageRepository;

    @Override
    @Transactional
    public TripPackageResponse createPackage(TripPackageRequest request) {
        TripPackage pkg = new TripPackage();
        pkg.setName(request.getName());
        pkg.setDescription(request.getDescription());
        pkg.setNumberOfDays(request.getNumberOfDays());
        pkg.setMinBudget(request.getMinBudget());
        pkg.setMaxBudget(request.getMaxBudget());
        pkg.setCoverImage(request.getCoverImage());
        pkg.setImages(request.getImages() != null ? request.getImages() : new ArrayList<>());
        pkg.setInterests(request.getInterests() != null ? request.getInterests() : new ArrayList<>());
        pkg.setItinerary(request.getItinerary() != null ? request.getItinerary() : new ArrayList<>());
        pkg.setIsFeatured(request.getIsFeatured() != null && request.getIsFeatured());
        pkg.setIsActive(true);

        TripPackage saved = tripPackageRepository.save(pkg);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public TripPackageResponse updatePackage(Long id, TripPackageRequest request) {
        TripPackage pkg = tripPackageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));

        pkg.setName(request.getName());
        pkg.setDescription(request.getDescription());
        pkg.setNumberOfDays(request.getNumberOfDays());
        pkg.setMinBudget(request.getMinBudget());
        pkg.setMaxBudget(request.getMaxBudget());
        pkg.setCoverImage(request.getCoverImage());
        pkg.setImages(request.getImages() != null ? request.getImages() : new ArrayList<>());
        pkg.setInterests(request.getInterests() != null ? request.getInterests() : new ArrayList<>());
        pkg.setItinerary(request.getItinerary() != null ? request.getItinerary() : new ArrayList<>());
        pkg.setIsFeatured(request.getIsFeatured() != null && request.getIsFeatured());

        TripPackage updated = tripPackageRepository.save(pkg);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deletePackage(Long id) {
        TripPackage pkg = tripPackageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));
        pkg.setIsActive(false);
        tripPackageRepository.save(pkg);
    }

    @Override
    public List<TripPackageResponse> getAllPackages() {
        return tripPackageRepository.findByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TripPackageResponse getPackageById(Long id) {
        TripPackage pkg = tripPackageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));
        return mapToResponse(pkg);
    }

    @Override
    public List<TripPackageResponse> getFeaturedPackages() {
        return tripPackageRepository.findByIsFeaturedTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TripPackageResponse> matchPackages(MatchTripRequest request) {
        int minDays = Math.max(1, request.getNumberOfDays() - 2);
        int maxDays = request.getNumberOfDays() + 2;

        List<TripPackage> matches = tripPackageRepository.findMatchingPackages(
                minDays, maxDays, request.getBudget());


        if (request.getInterests() != null && !request.getInterests().isEmpty()) {
            matches = matches.stream()
                    .filter(pkg -> pkg.getInterests().stream()
                            .anyMatch(interest -> request.getInterests().stream()
                                    .anyMatch(userInterest ->
                                            userInterest.toLowerCase().contains(interest.toLowerCase()) ||
                                                    interest.toLowerCase().contains(userInterest.toLowerCase()))))
                    .collect(Collectors.toList());
        }

        return matches.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TripPackageResponse mapToResponse(TripPackage pkg) {
        return TripPackageResponse.builder()
                .id(pkg.getId())
                .name(pkg.getName())
                .description(pkg.getDescription())
                .numberOfDays(pkg.getNumberOfDays())
                .minBudget(pkg.getMinBudget())
                .maxBudget(pkg.getMaxBudget())
                .coverImage(pkg.getCoverImage())
                .images(pkg.getImages())
                .interests(pkg.getInterests())
                .itinerary(pkg.getItinerary())
                .isFeatured(pkg.getIsFeatured())
                .isActive(pkg.getIsActive())
                .build();
    }
}