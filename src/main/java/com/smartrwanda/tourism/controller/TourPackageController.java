package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.TourPackageRequest;
import com.smartrwanda.tourism.dto.TourPackageResponse;
import com.smartrwanda.tourism.service.TourPackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tour-packages")
@RequiredArgsConstructor
@Tag(name = "Tour Packages", description = "Tour package management. Upload images via POST /api/upload/images first, then include returned URLs in the request body.")
public class TourPackageController {

    private final TourPackageService tourPackageService;

    @Operation(summary = "Create a tour package for a provider")
    @PostMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<TourPackageResponse>> create(
            @PathVariable Long providerId,
            @Valid @RequestBody TourPackageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tour package added",
                        tourPackageService.create(providerId, request)));
    }

    @Operation(summary = "Get tour package by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TourPackageResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(tourPackageService.getById(id)));
    }

    @Operation(summary = "Get all tour packages for a provider")
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<List<TourPackageResponse>>> getByProvider(
            @PathVariable Long providerId) {
        return ResponseEntity.ok(ApiResponse.success(tourPackageService.getByProvider(providerId)));
    }

    @Operation(summary = "Update a tour package")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TourPackageResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody TourPackageRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Tour package updated",
                tourPackageService.update(id, request)));
    }

    @Operation(summary = "Delete a tour package")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        tourPackageService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>success("Tour package deleted", null));
    }
}
