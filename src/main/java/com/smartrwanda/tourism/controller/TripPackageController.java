package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.MatchTripRequest;
import com.smartrwanda.tourism.dto.TripPackageRequest;
import com.smartrwanda.tourism.dto.TripPackageResponse;
import com.smartrwanda.tourism.service.TripPackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Trip Packages", description = "CRUD for trip packages. Upload images via POST /api/upload/images first, then include returned URLs in the request body.")
public class TripPackageController {

    private final TripPackageService tripPackageService;

    @Operation(summary = "Create a new trip package (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<TripPackageResponse>> createPackage(
            @Valid @RequestBody TripPackageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Trip package created",
                        tripPackageService.createPackage(request)));
    }

    @Operation(summary = "Update a trip package (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TripPackageResponse>> updatePackage(
            @PathVariable Long id,
            @Valid @RequestBody TripPackageRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Trip package updated",
                tripPackageService.updatePackage(id, request)));
    }

    @Operation(summary = "Soft-delete a trip package (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePackage(@PathVariable Long id) {
        tripPackageService.deletePackage(id);
        return ResponseEntity.ok(ApiResponse.<Void>success("Trip package deleted", null));
    }

    @Operation(summary = "Get all active trip packages")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TripPackageResponse>>> getAllPackages() {
        return ResponseEntity.ok(ApiResponse.success(tripPackageService.getAllPackages()));
    }

    @Operation(summary = "Get featured trip packages")
    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<TripPackageResponse>>> getFeaturedPackages() {
        return ResponseEntity.ok(ApiResponse.success(tripPackageService.getFeaturedPackages()));
    }

    @Operation(summary = "Get trip package by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TripPackageResponse>> getPackageById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(tripPackageService.getPackageById(id)));
    }

    @Operation(summary = "Match packages by days, budget and interests")
    @PostMapping("/match")
    public ResponseEntity<ApiResponse<List<TripPackageResponse>>> matchPackages(
            @Valid @RequestBody MatchTripRequest request) {
        return ResponseEntity.ok(ApiResponse.success(tripPackageService.matchPackages(request)));
    }
}
