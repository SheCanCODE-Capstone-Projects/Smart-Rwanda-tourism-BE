package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.request.MatchTripRequest;
import com.smartrwanda.tourism.dto.request.TripPackageRequest;
import com.smartrwanda.tourism.dto.response.TripPackageResponse;

import java.util.List;

public interface TripPackageService {


    TripPackageResponse createPackage(TripPackageRequest request);
    TripPackageResponse updatePackage(Long id, TripPackageRequest request);
    void deletePackage(Long id);


    List<TripPackageResponse> getAllPackages();
    TripPackageResponse getPackageById(Long id);
    List<TripPackageResponse> getFeaturedPackages();


    List<TripPackageResponse> matchPackages(MatchTripRequest request);
}