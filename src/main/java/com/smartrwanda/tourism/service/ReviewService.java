package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.ReviewFilterRequest;
import com.smartrwanda.tourism.dto.ReviewRequest;
import com.smartrwanda.tourism.dto.ReviewResponse;
import com.smartrwanda.tourism.dto.ReviewSummaryResponse;
import com.smartrwanda.tourism.enums.ReviewTargetType;

import java.util.List;

public interface ReviewService {

    ReviewResponse createReview(ReviewRequest request);

    ReviewResponse updateReview(Long reviewId, ReviewRequest request);

    void deleteReview(Long reviewId);

    List<ReviewResponse> getReviews(ReviewFilterRequest filter);

    ReviewSummaryResponse getSummary(ReviewTargetType targetType, Long targetId);

    void markHelpful(Long reviewId);

    void unmarkHelpful(Long reviewId);

    void reportReview(Long reviewId);

    void hideReview(Long reviewId);

    void unhideReview(Long reviewId);

    List<ReviewResponse> getReportedReviews();
}