package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.entity.ProviderCategory;
import com.smartrwanda.tourism.entity.VerificationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProviderSummaryResponse {
    private Long id;
    private String businessName;
    private ProviderCategory category;
    private String location;
    private VerificationStatus verificationStatus;
    private String coverImageUrl;
}
