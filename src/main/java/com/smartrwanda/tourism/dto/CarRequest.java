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
public class CarRequest {
    @NotBlank
    private String carName;

    @NotNull @Positive
    private BigDecimal pricePerDay;

    @NotNull @Positive
    private Integer numberOfSeats;

    @NotBlank
    private String transmission;

    private String description;

    @NotEmpty(message = "At least one car photo is required")
    private List<String> imageUrls;

    private String discount;
}
