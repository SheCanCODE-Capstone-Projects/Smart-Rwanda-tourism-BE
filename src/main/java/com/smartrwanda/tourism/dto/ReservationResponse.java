package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.entity.ReservationStatus;
import com.smartrwanda.tourism.entity.ProviderCategory;
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
public class ReservationResponse {

    private Long id;
    private Long userId;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private Long providerId;
    private String providerName;
    private String providerLocation;
    private ProviderCategory providerCategory;
    private String specialRequests;
    private ReservationStatus status;
    private String confirmationCode;
    private Double totalPrice;

    @Builder.Default
    private String currency = "RWF";

    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private Integer numberOfGuests;
    private String roomType;
    private LocalDateTime pickupDate;
    private LocalDateTime returnDate;
    private String vehicleType;
    private LocalDateTime tourDate;
    private String tourPackage;
    private Integer numberOfPeople;
    private LocalDateTime visitDate;
    private Integer numberOfVisitors;
    private String cancellationReason;
    private LocalDateTime cancelledAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime completedAt;
    private LocalDateTime rejectedAt;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}