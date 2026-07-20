package com.smartrwanda.tourism.dto.response;

import com.smartrwanda.tourism.enums.ReviewStatus;
import com.smartrwanda.tourism.enums.ReviewTargetType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ReviewResponse {

    private Long id;
    private String reviewerName;
    private ReviewTargetType targetType;
    private Long targetId;
    private Integer rating;
    private String comment;
    private ReviewStatus status;
    private Long helpfulCount;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
}