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
public class RoomRequest {
    @NotBlank
    private String roomType;

    @NotNull @Positive
    private BigDecimal pricePerNight;

    @NotNull @Positive
    private Integer numberOfBeds;

    private String description;

    @NotEmpty(message = "At least one room photo is required")
    private List<String> imageUrls;

    private String discount;
}