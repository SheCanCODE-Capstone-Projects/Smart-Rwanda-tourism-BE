package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.entity.DayActivity;
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
public class TripPackageResponse {

    private Long id;

    private String name;

    private String description;

    private Integer numberOfDays;

    private BigDecimal minBudget;

    private BigDecimal maxBudget;

    private String coverImage;

    private List<String> images;

    private List<String> interests;

    private List<DayActivity> itinerary;

    private Boolean isFeatured;

    private Boolean isActive;

    private Integer popularityScore;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}