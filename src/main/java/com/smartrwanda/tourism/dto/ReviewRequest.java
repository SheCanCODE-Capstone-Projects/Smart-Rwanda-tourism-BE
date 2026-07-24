package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.enums.ReviewTargetType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReviewRequest {

    @NotNull(message = "Target type is required")
    private ReviewTargetType targetType;

    @NotNull(message = "Target id is required")
    private Long targetId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Size(max = 2000, message = "Comment must be at most 2000 characters")
    private String comment;

    private List<String> imageUrls = new ArrayList<>();
}