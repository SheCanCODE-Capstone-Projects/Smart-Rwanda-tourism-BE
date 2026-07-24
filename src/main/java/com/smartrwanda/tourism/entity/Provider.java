package com.smartrwanda.tourism.entity;

import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "providers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Provider extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    private Provider employer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Enumerated(EnumType.STRING)
    private ProviderCategory category;

    private String description;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "contact_email")
    private String contactEmail;

    private String location;

    @ElementCollection
    @CollectionTable(name = "provider_images", joinColumns = @JoinColumn(name = "provider_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @ElementCollection
    @CollectionTable(name = "provider_skills", joinColumns = @JoinColumn(name = "provider_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    // Fields from the Admin/Reservation branch — kept for their features
    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    @Column(name = "total_reviews")
    private Integer totalReviews = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    private String website;
}