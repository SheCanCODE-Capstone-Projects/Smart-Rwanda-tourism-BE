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

    // ============================================================
    // Pricing
    // ============================================================

    private Double totalPrice;
    private String currency = "RWF";

    // ============================================================
    // STAY (Hotels, Motels, Apartments)
    // ============================================================

    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private Integer numberOfGuests;
    private String roomType;

    // ============================================================
    // MOVE (Car Rentals)
    // ============================================================

    private LocalDateTime pickupDate;
    private LocalDateTime returnDate;
    private String vehicleType;

    // ============================================================
    // TOURS (Touring Agencies)
    // ============================================================

    private LocalDateTime tourDate;
    private String tourPackage;
    private Integer numberOfPeople;

    // ============================================================
    // ATTRACTIONS (Places to Visit)
    // ============================================================

    private LocalDateTime visitDate;
    private Integer numberOfVisitors;
}