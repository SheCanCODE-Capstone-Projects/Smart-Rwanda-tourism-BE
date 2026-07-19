package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.entity.ProviderCategory;
import com.smartrwanda.tourism.entity.VerificationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ProviderResponse {
    private Long id;
    private Long userId;
    private String businessName;
    private ProviderCategory category;
    private String description;
    private String phone;
    private String email;
    private String location;
    private List<String> imageUrls;
    private VerificationStatus verificationStatus;
    private LocalDateTime createdAt;
}
