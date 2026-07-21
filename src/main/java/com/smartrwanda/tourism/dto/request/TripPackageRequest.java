package com.smartrwanda.tourism.dto.request;

import com.smartrwanda.tourism.entity.DayActivity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripPackageRequest {

    @NotBlank(message = "Package name is required")
    private String name;

    private String description;

    @NotNull(message = "Number of days is required")
    @Positive(message = "Number of days must be greater than 0")
    private Integer numberOfDays;

    @NotNull(message = "Minimum budget is required")
    @Positive(message = "Minimum budget must be greater than 0")
    private BigDecimal minBudget;

    @NotNull(message = "Maximum budget is required")
    @Positive(message = "Maximum budget must be greater than 0")
    private BigDecimal maxBudget;

    private String coverImage;

    private List<String> images;

    private List<String> interests;

    private List<DayActivity> itinerary;

    private Boolean isFeatured = false;
}