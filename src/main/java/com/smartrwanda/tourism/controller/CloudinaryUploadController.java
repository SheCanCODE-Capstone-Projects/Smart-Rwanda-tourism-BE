package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@Tag(name = "Cloudinary Upload", description = "Upload images to Cloudinary and get back URLs to use in your JSON requests")
public class CloudinaryUploadController {

    private final CloudinaryService cloudinaryService;

    @Operation(summary = "Upload a single image, returns Cloudinary URL")
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadSingleImage(
            @RequestPart("file") MultipartFile file,
            @RequestParam(defaultValue = "general") String folder) {
        String imageUrl = cloudinaryService.uploadImage(file, folder);
        return ResponseEntity.ok(ApiResponse.success(
                "Image uploaded successfully. Use this URL in your JSON payload.", imageUrl));
    }

    @Operation(summary = "Upload multiple images, returns array of Cloudinary URLs")
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<String>>> uploadMultipleImages(
            @RequestPart("files") List<MultipartFile> files,
            @RequestParam(defaultValue = "general") String folder) {
        List<String> imageUrls = cloudinaryService.uploadMultipleImages(files, folder);
        return ResponseEntity.ok(ApiResponse.success(
                "Images uploaded successfully. Use these URLs in your JSON payload.", imageUrls));
    }

    @Operation(summary = "Delete an image from Cloudinary by public ID")
    @DeleteMapping("/image/{publicId}")
    public ResponseEntity<ApiResponse<Void>> deleteImage(@PathVariable String publicId) {
        cloudinaryService.deleteImage(publicId);
        return ResponseEntity.ok(ApiResponse.<Void>success("Image deleted successfully.", null));
    }
}
