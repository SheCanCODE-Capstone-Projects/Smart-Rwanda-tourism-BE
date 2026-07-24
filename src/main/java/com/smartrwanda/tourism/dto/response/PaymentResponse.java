package com.smartrwanda.tourism.dto.response;

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
public class PaymentResponse {

    private Long id;
    private Long reservationId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionReference;
    private String notes;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}