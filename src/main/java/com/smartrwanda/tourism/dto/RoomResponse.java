package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.entity.ProviderCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
public class RoomResponse {
    private Long id;
    private Long providerId;
    private String roomType;
    private BigDecimal pricePerNight;
    private Integer numberOfBeds;
    private String description;
    private List<String> imageUrls;
    private String discount;
    private boolean available;
    private LocalDateTime createdAt;
}
