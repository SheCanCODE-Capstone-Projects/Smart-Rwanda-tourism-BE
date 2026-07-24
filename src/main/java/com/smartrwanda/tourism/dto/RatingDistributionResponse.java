package com.smartrwanda.tourism.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RatingDistributionResponse {

    private Long fiveStars;
    private Long fourStars;
    private Long threeStars;
    private Long twoStars;
    private Long oneStar;
}