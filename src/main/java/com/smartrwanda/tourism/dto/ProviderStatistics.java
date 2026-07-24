package com.smartrwanda.tourism.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderStatistics {

    private long totalProviders;
    private long pendingCount;
    private long verifiedCount;
    private long rejectedCount;
}