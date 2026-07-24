package com.smartrwanda.tourism.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private Long providerId;
    private Long userId;
    private Long reservationId;
    private String title;
    private String message;
    private String type;
    private Boolean isRead;
    private String actionUrl;
    private String icon;
    private String targetType;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}