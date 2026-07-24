package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.DiscoverFilterRequest;
import com.smartrwanda.tourism.dto.DiscoverItemRequest;
import com.smartrwanda.tourism.dto.DiscoverCategoryResponse;
import com.smartrwanda.tourism.dto.DiscoverItemResponse;
import com.smartrwanda.tourism.entity.MainCategory;
import com.smartrwanda.tourism.service.DiscoverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discover")
@RequiredArgsConstructor
public class DiscoverController {

    private final DiscoverService discoverService;



    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<DiscoverItemResponse>> createDiscoverItem(
            @Valid @RequestBody DiscoverItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Discover item created successfully",
                        discoverService.createDiscoverItem(request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DiscoverItemResponse>> updateDiscoverItem(
            @PathVariable Long id,
            @Valid @RequestBody DiscoverItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Discover item updated successfully",
                discoverService.updateDiscoverItem(id, request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDiscoverItem(@PathVariable Long id) {
        discoverService.deleteDiscoverItem(id);
        return ResponseEntity.ok(ApiResponse.success("Discover item deleted successfully", null));
    }



    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DiscoverItemResponse>> getDiscoverItem(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getDiscoverItemById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DiscoverItemResponse>>> getAllDiscoverItems() {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getAllDiscoverItems()));
    }

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<DiscoverItemResponse>>> getAllDiscoverItemsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getAllDiscoverItemsPaginated(page, size)));
    }

    @GetMapping("/category/{mainCategory}")
    public ResponseEntity<ApiResponse<List<DiscoverItemResponse>>> getByMainCategory(
            @PathVariable MainCategory mainCategory) {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getByMainCategory(mainCategory)));
    }

    @GetMapping("/category/{mainCategory}/page")
    public ResponseEntity<ApiResponse<Page<DiscoverItemResponse>>> getByMainCategoryPaginated(
            @PathVariable MainCategory mainCategory,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getByMainCategoryPaginated(mainCategory, page, size)));
    }

    @GetMapping("/category/{mainCategory}/{subCategory}")
    public ResponseEntity<ApiResponse<List<DiscoverItemResponse>>> getByMainCategoryAndSubCategory(
            @PathVariable MainCategory mainCategory,
            @PathVariable String subCategory) {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getByMainCategoryAndSubCategory(mainCategory, subCategory)));
    }

    @GetMapping("/category/{mainCategory}/{subCategory}/page")
    public ResponseEntity<ApiResponse<Page<DiscoverItemResponse>>> getByMainCategoryAndSubCategoryPaginated(
            @PathVariable MainCategory mainCategory,
            @PathVariable String subCategory,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getByMainCategoryAndSubCategoryPaginated(
                mainCategory, subCategory, page, size)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<DiscoverItemResponse>>> search(
            @RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(discoverService.searchDiscoverItems(keyword)));
    }

    @GetMapping("/search/{mainCategory}")
    public ResponseEntity<ApiResponse<List<DiscoverItemResponse>>> searchByCategory(
            @PathVariable MainCategory mainCategory,
            @RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(discoverService.searchByCategoryAndKeyword(mainCategory, keyword)));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<DiscoverItemResponse>>> getFeatured() {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getFeaturedItems()));
    }

    @GetMapping("/featured/{mainCategory}")
    public ResponseEntity<ApiResponse<List<DiscoverItemResponse>>> getFeaturedByCategory(
            @PathVariable MainCategory mainCategory) {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getFeaturedByCategory(mainCategory)));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<DiscoverItemResponse>>> getRecent() {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getRecentItems()));
    }

    @GetMapping("/recent/{mainCategory}")
    public ResponseEntity<ApiResponse<List<DiscoverItemResponse>>> getRecentByCategory(
            @PathVariable MainCategory mainCategory) {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getRecentByCategory(mainCategory)));
    }

    @GetMapping("/top-rated")
    public ResponseEntity<ApiResponse<List<DiscoverItemResponse>>> getTopRated() {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getTopRatedAll()));
    }

    @GetMapping("/top-rated/{mainCategory}")
    public ResponseEntity<ApiResponse<List<DiscoverItemResponse>>> getTopRatedByCategory(
            @PathVariable MainCategory mainCategory) {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getTopRatedByCategory(mainCategory)));
    }

    @PostMapping("/filter")
    public ResponseEntity<ApiResponse<Page<DiscoverItemResponse>>> filterDiscoverItems(
            @RequestBody DiscoverFilterRequest filter) {
        return ResponseEntity.ok(ApiResponse.success(discoverService.filterDiscoverItems(filter)));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<DiscoverCategoryResponse>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getAllCategoriesWithCounts()));
    }

    @GetMapping("/categories/{mainCategory}")
    public ResponseEntity<ApiResponse<DiscoverCategoryResponse>> getCategoryDetails(
            @PathVariable MainCategory mainCategory) {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getCategoryDetails(mainCategory)));
    }

    @GetMapping("/location")
    public ResponseEntity<ApiResponse<List<DiscoverItemResponse>>> getByLocation(
            @RequestParam String location) {
        return ResponseEntity.ok(ApiResponse.success(discoverService.getByLocation(location)));
    }
}