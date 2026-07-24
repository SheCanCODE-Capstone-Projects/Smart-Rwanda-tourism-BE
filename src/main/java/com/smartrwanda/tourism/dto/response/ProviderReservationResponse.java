package com.smartrwanda.tourism.dto.response;

import com.smartrwanda.tourism.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderReservationResponse {

    private Long id;
    private String confirmationCode;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private Integer numberOfGuests;
    private String roomType;
    private String specialRequests;
    private BigDecimal totalPrice;
    private String currency;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
    private LocalDateTime rejectedAt;
    private String rejectionReason;
    private String cancellationReason;
}