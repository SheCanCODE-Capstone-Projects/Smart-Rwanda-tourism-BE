package com.smartrwanda.tourism.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderReservationActionRequest {

    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;
}