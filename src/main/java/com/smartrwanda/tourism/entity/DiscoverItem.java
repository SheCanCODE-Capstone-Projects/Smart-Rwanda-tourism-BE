package com.smartrwanda.tourism.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "discover_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscoverItem extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "main_category", nullable = false)
    private MainCategory mainCategory;

    @Column(name = "sub_category", nullable = false)
    private String subCategory;

    @Column(nullable = false)
    private String location;

    private Double latitude;

    private Double longitude;

    @Column(columnDefinition = "TEXT[]")
    private List<String> images = new ArrayList<>();

    @Column(name = "opening_hours")
    private String openingHours;

    private BigDecimal price;

    @Column(name = "contact_info")
    private String contactInfo;

    private String website;

    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    @Column(name = "total_reviews")
    private Integer totalReviews = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    private Boolean featured = false;


    @Column(name = "star_rating")
    private Integer starRating;


    @Column(name = "vehicle_types")
    private String vehicleTypes;


    @Column(name = "cuisine_type")
    private String cuisineType;


    @Column(name = "entrance_fee")
    private BigDecimal entranceFee;
}