package com.smartrwanda.tourism.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
public class DishResponse {
    private Long id;
    private Long providerId;
    private String name;
    private BigDecimal price;
    private String description;
    private boolean spicy;
    private List<String> imageUrls;
    private String discount;
    private boolean available;
    private LocalDateTime createdAt;
}
