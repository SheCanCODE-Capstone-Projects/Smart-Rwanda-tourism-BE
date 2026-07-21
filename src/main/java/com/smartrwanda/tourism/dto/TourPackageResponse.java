package com.smartrwanda.tourism.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
public class TourPackageResponse {
    private Long id;
    private Long providerId;
    private String title;
    private String description;
    private Integer durationDays;
    private BigDecimal priceRwandan;
    private BigDecimal priceAfrican;
    private BigDecimal priceInternational;
    private List<String> imageUrls;
    private String discount;
    private List<String> inclusions;
    private boolean available;
    private LocalDateTime createdAt;
}
