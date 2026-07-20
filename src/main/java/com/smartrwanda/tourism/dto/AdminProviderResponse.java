package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.entity.ProviderCategory;
import com.smartrwanda.tourism.entity.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminProviderResponse {

    private Long id;
    private String businessName;
    private String description;
    private ProviderCategory category;
    private String contactEmail;
    private String contactPhone;
    private String location;
    private String website;
    private VerificationStatus verificationStatus;
    private String logoUrl;
    private String coverImageUrl;
    private Double averageRating;
    private Integer totalReviews;
    private Boolean isActive;
    private Long userId;
    private String ownerName;
    private String ownerEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long totalBookings;
    private Long totalServices;
}