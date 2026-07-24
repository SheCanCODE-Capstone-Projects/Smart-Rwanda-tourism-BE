package com.smartrwanda.tourism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dishes")
@Getter @Setter
@NoArgsConstructor
public class Dish extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String description;

    private boolean spicy = false;

    @ElementCollection
    @CollectionTable(name = "dish_images", joinColumns = @JoinColumn(name = "dish_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    private String discount; // optional, e.g. "Buy 2 get fries free"

    @Column(nullable = false)
    private boolean available = true;
}
