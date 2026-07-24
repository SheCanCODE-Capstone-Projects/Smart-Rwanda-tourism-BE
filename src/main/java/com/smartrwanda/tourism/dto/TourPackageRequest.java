package com.smartrwanda.tourism.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class TourPackageRequest {
    @NotBlank
    private String title;

    private String description;

    @NotNull @Positive
    private Integer durationDays;

    @NotNull @Positive
    private BigDecimal priceRwandan;

    @NotNull @Positive
    private BigDecimal priceAfrican;

    @NotNull @Positive
    private BigDecimal priceInternational;

    @NotEmpty(message = "At least one tour photo is required")
    private List<String> imageUrls;

    private String discount;
    private List<String> inclusions; // optional, e.g. ["Car", "Guide", "Meals", "Snacks", "Accommodation"]
}
