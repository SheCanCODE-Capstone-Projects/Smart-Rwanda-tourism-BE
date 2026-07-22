package com.smartrwanda.tourism.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {

    private Long id;
    private Long reservationId;
    private Long providerId;
    private String providerName;
    private Long userId;
    private String userName;
    private List<MessageResponse> messages;
    private Long unreadCount;
    private LocalDateTime lastMessageAt;
    private LocalDateTime createdAt;
}