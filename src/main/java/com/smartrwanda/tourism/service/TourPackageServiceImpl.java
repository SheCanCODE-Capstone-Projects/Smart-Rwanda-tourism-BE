package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.TourPackageRequest;
import com.smartrwanda.tourism.dto.TourPackageResponse;
import com.smartrwanda.tourism.entity.Provider;
import com.smartrwanda.tourism.entity.ProviderCategory;
import com.smartrwanda.tourism.entity.TourPackage;
import com.smartrwanda.tourism.exception.BadRequestException;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.mapper.TourPackageMapper;
import com.smartrwanda.tourism.repository.ProviderRepository;
import com.smartrwanda.tourism.repository.TourPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TourPackageServiceImpl implements TourPackageService {

    private final TourPackageRepository tourPackageRepository;
    private final ProviderRepository providerRepository;
    private final TourPackageMapper tourPackageMapper;

    @Override
    public TourPackageResponse create(Long providerId, TourPackageRequest request) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));

        if (provider.getCategory() != ProviderCategory.TOUR_AGENCY) {
            throw new BadRequestException("Only tour agencies can add tour packages");
        }

        TourPackage tourPackage = tourPackageMapper.toEntity(request);
        tourPackage.setProvider(provider);

        return tourPackageMapper.toResponse(tourPackageRepository.save(tourPackage));
    }

    @Override
    public TourPackageResponse getById(Long id) {
        TourPackage tourPackage = tourPackageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour package not found"));
        return tourPackageMapper.toResponse(tourPackage);
    }

    @Override
    public List<TourPackageResponse> getByProvider(Long providerId) {
        return tourPackageRepository.findByProviderId(providerId).stream()
                .map(tourPackageMapper::toResponse)
                .toList();
    }

    @Override
    public TourPackageResponse update(Long id, TourPackageRequest request) {
        TourPackage tourPackage = tourPackageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour package not found"));

        tourPackage.setTitle(request.getTitle());
        tourPackage.setDescription(request.getDescription());
        tourPackage.setDurationDays(request.getDurationDays());
        tourPackage.setPriceRwandan(request.getPriceRwandan());
        tourPackage.setPriceAfrican(request.getPriceAfrican());
        tourPackage.setPriceInternational(request.getPriceInternational());
        tourPackage.setImageUrls(request.getImageUrls());
        tourPackage.setDiscount(request.getDiscount());
        tourPackage.setInclusions(request.getInclusions());

        return tourPackageMapper.toResponse(tourPackageRepository.save(tourPackage));
    }

    @Override
    public void delete(Long id) {
        if (!tourPackageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tour package not found");
        }
        tourPackageRepository.deleteById(id);
    }
}