package com.smartrwanda.tourism.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginHistoryResponse {
    private Long id;
    private Long userId;  // Added this field
    private String ipAddress;
    private String userAgent;
    private String location;
    private LocalDateTime loginTime;
    private boolean successful;
}
