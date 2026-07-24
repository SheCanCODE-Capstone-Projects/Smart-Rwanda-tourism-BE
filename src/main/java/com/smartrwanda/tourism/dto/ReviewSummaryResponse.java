package com.smartrwanda.tourism.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewSummaryResponse {

    private Double averageRating;
    private Long totalReviews;
    private RatingDistributionResponse distribution;
}