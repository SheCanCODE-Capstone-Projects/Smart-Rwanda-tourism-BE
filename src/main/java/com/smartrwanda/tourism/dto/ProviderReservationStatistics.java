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
public class ProviderReservationStatistics {

    private Long providerId;

    private long totalReservations;
    private long pendingCount;
    private long confirmedCount;
    private long completedCount;
    private long cancelledCount;
    private long rejectedCount;

    private Double totalRevenue;
    private String currency;
}
