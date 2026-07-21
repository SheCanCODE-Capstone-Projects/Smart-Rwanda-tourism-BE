package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.AdminAttractionUpdateRequest;
import com.smartrwanda.tourism.dto.DiscoverItemRequest;
import com.smartrwanda.tourism.dto.AdminAttractionResponse;
import com.smartrwanda.tourism.dto.DiscoverItemResponse;
import com.smartrwanda.tourism.service.AdminService;
import com.smartrwanda.tourism.service.CloudinaryService;
import com.smartrwanda.tourism.service.DiscoverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/attractions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAttractionController {

    private final DiscoverService discoverService;
    private final AdminService adminService;
    private final CloudinaryService cloudinaryService;


    @PostMapping
    public ResponseEntity<ApiResponse<DiscoverItemResponse>> createAttraction(
            @Valid @RequestBody DiscoverItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Attraction created successfully",
                        discoverService.createDiscoverItem(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DiscoverItemResponse>> updateAttraction(
            @PathVariable Long id,
            @Valid @RequestBody DiscoverItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Attraction updated successfully",
                discoverService.updateDiscoverItem(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAttraction(@PathVariable Long id) {
        discoverService.deleteDiscoverItem(id);
        return ResponseEntity.ok(ApiResponse.success("Attraction deleted successfully", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DiscoverItemResponse>>> getAllAttractions() {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getAllDiscoverItems()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DiscoverItemResponse>> getAttractionById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getDiscoverItemById(id)));
    }



    @PostMapping("/{id}/images")
    public ResponseEntity<ApiResponse<List<String>>> uploadImages(
            @PathVariable Long id,
            @RequestParam("images") List<MultipartFile> images) {

        List<String> imageUrls = cloudinaryService.uploadMultipleImages(images, "attractions");


        DiscoverItemResponse attraction = discoverService.getDiscoverItemById(id);

        List<String> existingImages = attraction.getImages();
        if (existingImages != null) {
            existingImages.addAll(imageUrls);
        } else {
            existingImages = imageUrls;
        }



        return ResponseEntity.ok(ApiResponse.success("Images uploaded successfully", imageUrls));
    }

    @DeleteMapping("/{id}/images")
    public ResponseEntity<ApiResponse<Void>> deleteImage(
            @PathVariable Long id,
            @RequestParam String publicId) {

        cloudinaryService.deleteImage(publicId);


        return ResponseEntity.ok(ApiResponse.success("Image deleted successfully", null));
    }
}