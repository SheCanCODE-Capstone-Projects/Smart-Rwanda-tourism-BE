package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.TourPackageRequest;
import com.smartrwanda.tourism.dto.TourPackageResponse;
import com.smartrwanda.tourism.service.TourPackageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tour-packages")
@RequiredArgsConstructor
public class TourPackageController {

    private final TourPackageService tourPackageService;

    @PostMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<TourPackageResponse>> create(
            @PathVariable Long providerId, @Valid @RequestBody TourPackageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tour package added", tourPackageService.create(providerId, request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TourPackageResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(tourPackageService.getById(id)));
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<List<TourPackageResponse>>> getByProvider(@PathVariable Long providerId) {
        return ResponseEntity.ok(ApiResponse.success(tourPackageService.getByProvider(providerId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TourPackageResponse>> update(
            @PathVariable Long id, @Valid @RequestBody TourPackageRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Tour package updated", tourPackageService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        tourPackageService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Tour package deleted", null));
    }
}
