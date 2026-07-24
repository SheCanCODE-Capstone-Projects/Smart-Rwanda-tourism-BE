package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderReservationResponse {

    private Long id;
    private String confirmationCode;
    private ReservationStatus status;


    private String guestName;
    private String guestEmail;
    private String guestPhone;


    private LocalDateTime reservationDate;
    private String specialRequests;
    private Double totalPrice;
    private String currency;


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


    private LocalDateTime confirmedAt;
    private LocalDateTime completedAt;
    private LocalDateTime rejectedAt;
    private String rejectionReason;
    private LocalDateTime cancelledAt;
    private String cancellationReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}