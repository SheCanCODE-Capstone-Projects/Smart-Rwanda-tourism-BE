package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.entity.MainCategory;
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
public class AdminAttractionUpdateRequest {

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
    private Boolean isActive;
    private Boolean featured;
}