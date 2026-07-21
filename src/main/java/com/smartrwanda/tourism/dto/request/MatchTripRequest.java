package com.smartrwanda.tourism.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchTripRequest {

    @NotNull(message = "Number of days is required")
    @Min(value = 1, message = "At least 1 day is required")
    private Integer numberOfDays;

    @NotNull(message = "Budget is required")
    @Min(value = 1, message = "Budget must be greater than 0")
    private BigDecimal budget;

    private List<String> interests;
}