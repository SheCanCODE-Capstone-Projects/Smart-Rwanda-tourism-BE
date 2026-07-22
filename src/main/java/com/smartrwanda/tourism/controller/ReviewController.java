package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.ReviewFilterRequest;
import com.smartrwanda.tourism.dto.ReviewRequest;
import com.smartrwanda.tourism.dto.ReviewResponse;
import com.smartrwanda.tourism.dto.ReviewSummaryResponse;
import com.smartrwanda.tourism.enums.ReviewTargetType;
import com.smartrwanda.tourism.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/public/reviews")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviews(
            @Valid ReviewFilterRequest filter) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviews(filter)));
    }

    @GetMapping("/public/reviews/summary")
    public ResponseEntity<ApiResponse<ReviewSummaryResponse>> getSummary(
            @RequestParam ReviewTargetType targetType,
            @RequestParam Long targetId) {
        return ResponseEntity.ok(ApiResponse.success(
                reviewService.getSummary(targetType, targetId)));
    }

    @PostMapping("/reviews")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Review created successfully",
                        reviewService.createReview(request)));
    }

    @PutMapping("/reviews/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Review updated successfully",
                reviewService.updateReview(id, request)));
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.success("Review deleted successfully", null));
    }

    @PostMapping("/reviews/{id}/helpful")
    public ResponseEntity<ApiResponse<Void>> markHelpful(@PathVariable Long id) {
        reviewService.markHelpful(id);
        return ResponseEntity.ok(ApiResponse.success("Review marked as helpful", null));
    }

    @DeleteMapping("/reviews/{id}/helpful")
    public ResponseEntity<ApiResponse<Void>> unmarkHelpful(@PathVariable Long id) {
        reviewService.unmarkHelpful(id);
        return ResponseEntity.ok(ApiResponse.success("Review unmarked as helpful", null));
    }

    @PostMapping("/reviews/{id}/report")
    public ResponseEntity<ApiResponse<Void>> reportReview(@PathVariable Long id) {
        reviewService.reportReview(id);
        return ResponseEntity.ok(ApiResponse.success("Review reported. An admin will look into it.", null));
    }

    @PatchMapping("/reviews/{id}/hide")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> hideReview(@PathVariable Long id) {
        reviewService.hideReview(id);
        return ResponseEntity.ok(ApiResponse.success("Review hidden", null));
    }

    @PatchMapping("/reviews/{id}/unhide")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> unhideReview(@PathVariable Long id) {
        reviewService.unhideReview(id);
        return ResponseEntity.ok(ApiResponse.success("Review unhidden", null));
    }

    @GetMapping("/reviews/reported")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReportedReviews() {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReportedReviews()));
    }
}