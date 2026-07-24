package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.MatchTripRequest;
import com.smartrwanda.tourism.dto.TripPackageRequest;
import com.smartrwanda.tourism.dto.TripPackageResponse;
import com.smartrwanda.tourism.service.TripPackageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip-packages")
@RequiredArgsConstructor
public class TripPackageController {

    private final TripPackageService tripPackageService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<TripPackageResponse>> createPackage(
            @Valid @RequestBody TripPackageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Trip package created", tripPackageService.createPackage(request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TripPackageResponse>> updatePackage(
            @PathVariable Long id,
            @Valid @RequestBody TripPackageRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Trip package updated",
                tripPackageService.updatePackage(id, request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePackage(@PathVariable Long id) {
        tripPackageService.deletePackage(id);
        return ResponseEntity.ok(ApiResponse.success("Trip package deleted", null));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<TripPackageResponse>>> getAllPackages() {
        return ResponseEntity.ok(ApiResponse.success(tripPackageService.getAllPackages()));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<TripPackageResponse>>> getFeaturedPackages() {
        return ResponseEntity.ok(ApiResponse.success(tripPackageService.getFeaturedPackages()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TripPackageResponse>> getPackageById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(tripPackageService.getPackageById(id)));
    }


    @PostMapping("/match")
    public ResponseEntity<ApiResponse<List<TripPackageResponse>>> matchPackages(
            @Valid @RequestBody MatchTripRequest request) {
        return ResponseEntity.ok(ApiResponse.success(tripPackageService.matchPackages(request)));
    }
}