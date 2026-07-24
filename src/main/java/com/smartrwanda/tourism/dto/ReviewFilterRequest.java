package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.enums.ReviewTargetType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewFilterRequest {

    private ReviewTargetType targetType;

    private Long targetId;

    @Min(value = 1, message = "Rating filter must be at least 1")
    @Max(value = 5, message = "Rating filter must be at most 5")
    private Integer rating;
}