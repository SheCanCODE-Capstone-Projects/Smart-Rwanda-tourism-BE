package com.smartrwanda.tourism.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
public class CarResponse {
    private Long id;
    private Long providerId;
    private String carName;
    private BigDecimal pricePerDay;
    private Integer numberOfSeats;
    private String transmission;
    private String description;
    private List<String> imageUrls;
    private String discount;
    private boolean available;
    private LocalDateTime createdAt;
}
