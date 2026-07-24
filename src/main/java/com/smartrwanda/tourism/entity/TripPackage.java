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
@Table(name = "trip_packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripPackage extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(name = "number_of_days", nullable = false)
    private Integer numberOfDays;

    @Column(name = "min_budget", nullable = false)
    private BigDecimal minBudget;

    @Column(name = "max_budget", nullable = false)
    private BigDecimal maxBudget;

    @Column(name = "cover_image")
    private String coverImage;

    @ElementCollection
    @CollectionTable(name = "package_images", joinColumns = @JoinColumn(name = "package_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "package_interests", joinColumns = @JoinColumn(name = "package_id"))
    @Column(name = "interest")
    private List<String> interests = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "package_itinerary", joinColumns = @JoinColumn(name = "package_id"))
    private List<DayActivity> itinerary = new ArrayList<>();

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "is_active")
    private Boolean isActive = true;
}