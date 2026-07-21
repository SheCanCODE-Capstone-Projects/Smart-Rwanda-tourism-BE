package com.smartrwanda.tourism.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class DishRequest {
    @NotBlank
    private String name;

    @NotNull @Positive
    private BigDecimal price;

    private String description;
    private boolean spicy;

    @NotEmpty(message = "At least one dish photo is required")
    private List<String> imageUrls;

    private String discount;
}