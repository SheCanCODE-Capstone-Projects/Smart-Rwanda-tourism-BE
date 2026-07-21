package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.entity.ProviderCategory;
import com.smartrwanda.tourism.entity.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderResponse {

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
    private String openingHours;
    private Boolean isActive;
    private Long userId;
    private String ownerName;
    private String ownerEmail;
    private Integer serviceCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> imageUrls;
}