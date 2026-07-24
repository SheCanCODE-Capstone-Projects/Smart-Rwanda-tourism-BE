package com.smartrwanda.tourism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tour_packages")
@Getter @Setter
@NoArgsConstructor
public class TourPackage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider; // the Tour Agency

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;


    @Column(name = "price_rwandan", nullable = false)
    private BigDecimal priceRwandan;

    @Column(name = "price_african", nullable = false)
    private BigDecimal priceAfrican;

    @Column(name = "price_international", nullable = false)
    private BigDecimal priceInternational;


    @ElementCollection
    @CollectionTable(name = "tour_package_inclusions", joinColumns = @JoinColumn(name = "tour_package_id"))
    @Column(name = "inclusion")
    private List<String> inclusions = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "tour_package_images", joinColumns = @JoinColumn(name = "tour_package_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    private String discount;


    @Column(nullable = false)
    private boolean available = true;
}
