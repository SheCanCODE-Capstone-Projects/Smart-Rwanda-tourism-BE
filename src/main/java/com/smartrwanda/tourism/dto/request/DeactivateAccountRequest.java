package com.smartrwanda.tourism.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeactivateAccountRequest {
    @NotBlank(message = "Password is required")
    private String password;
    private String reason;
}