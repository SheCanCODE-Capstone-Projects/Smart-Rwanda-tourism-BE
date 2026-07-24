package com.smartrwanda.tourism.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatistics {

    private Long totalUsers;
    private Long activeUsers;
    private Long totalProviders;
    private Long verifiedProviders;
    private Long pendingProviders;
    private Long totalAttractions;
    private Long totalBookings;
    private Long pendingBookings;
    private Long confirmedBookings;
    private Long completedBookings;
    private Long totalReviews;
    private Double averageRating;
    private Long todayBookings;
    private Long thisMonthBookings;
    private Double totalRevenue;
    private Long totalServices;
}