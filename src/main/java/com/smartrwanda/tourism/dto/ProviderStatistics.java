package com.smartrwanda.tourism.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProviderStatistics {
    private long totalProviders;
    private long pendingCount;
    private long verifiedCount;
    private long rejectedCount;
}
