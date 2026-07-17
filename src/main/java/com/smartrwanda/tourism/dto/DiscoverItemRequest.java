package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.entity.MainCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class DiscoverItemRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Main category is required")
    private MainCategory mainCategory;

    @NotBlank(message = "Sub-category is required")
    private String subCategory;

    @NotBlank(message = "Location is required")
    private String location;

    private Double latitude;

    private Double longitude;

    private List<String> images;

    private String openingHours;

    private BigDecimal price;

    private String contactInfo;

    private String website;

    private Boolean featured = false;

    private Integer starRating;

    private String vehicleTypes;

    private String cuisineType;

    private BigDecimal entranceFee;
}