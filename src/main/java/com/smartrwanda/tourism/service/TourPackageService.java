package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.TourPackageRequest;
import com.smartrwanda.tourism.dto.TourPackageResponse;
import java.util.List;

public interface TourPackageService {
    TourPackageResponse create(Long providerId, TourPackageRequest request);
    TourPackageResponse getById(Long id);
    List<TourPackageResponse> getByProvider(Long providerId);
    TourPackageResponse update(Long id, TourPackageRequest request);
    void delete(Long id);
}
