package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.entity.MainCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAttractionResponse {

    private Long id;
    private String name;
    private String description;
    private MainCategory mainCategory;
    private String subCategory;
    private String location;
    private Double latitude;
    private Double longitude;
    private List<String> images;
    private String openingHours;
    private BigDecimal price;
    private String contactInfo;
    private String website;
    private Double averageRating;
    private Integer totalReviews;
    private Boolean isActive;
    private Boolean featured;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}