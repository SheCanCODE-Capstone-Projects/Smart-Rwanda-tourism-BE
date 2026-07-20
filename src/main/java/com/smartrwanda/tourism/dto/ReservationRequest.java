package com.smartrwanda.tourism.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

    @NotNull(message = "Provider ID is required")
    private Long providerId;
    private String specialRequests;
    private Double totalPrice;
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
}