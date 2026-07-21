package com.smartrwanda.tourism.entity;

import com.smartrwanda.tourism.entity.ProviderCategory;
import com.smartrwanda.tourism.entity.VerificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "providers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Provider extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProviderCategory category;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    private String location;

    private String website;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "average_rating")
    private Double averageRating;

    @Column(name = "total_reviews")
    private Integer totalReviews;

    @Column(name = "opening_hours")
    private String openingHours;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


}
