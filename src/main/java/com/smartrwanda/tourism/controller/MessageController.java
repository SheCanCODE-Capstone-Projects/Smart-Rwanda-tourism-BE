package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.MessageRequest;
import com.smartrwanda.tourism.dto.ConversationResponse;
import com.smartrwanda.tourism.dto.MessageResponse;
import com.smartrwanda.tourism.service.MessagingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessagingService messagingService;

    @PostMapping
    public ResponseEntity<ApiResponse<MessageResponse>> sendMessage(
            @Valid @RequestBody MessageRequest request,
            @RequestHeader("Authorization") String token) {
        Long senderId = getCurrentUserId(token);
        String senderType = getSenderType(token);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Message sent",
                        messagingService.sendMessage(request, senderId, senderType)));
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getMessages(
            @PathVariable Long conversationId) {
        return ResponseEntity.ok(ApiResponse.success(messagingService.getMessages(conversationId)));
    }

    @GetMapping("/conversations/user")
    public ResponseEntity<ApiResponse<List<ConversationResponse>>> getUserConversations(
            @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        return ResponseEntity.ok(ApiResponse.success(messagingService.getUserConversations(userId)));
    }

    @GetMapping("/conversations/provider")
    public ResponseEntity<ApiResponse<List<ConversationResponse>>> getProviderConversations(
            @RequestHeader("Authorization") String token) {
        Long providerId = getProviderId(token);
        return ResponseEntity.ok(ApiResponse.success(messagingService.getProviderConversations(providerId)));
    }

    @GetMapping("/conversation/{conversationId}/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
            @PathVariable Long conversationId,
            @RequestHeader("Authorization") String token) {
        String senderType = getSenderType(token);
        return ResponseEntity.ok(ApiResponse.success(
                messagingService.getUnreadCount(conversationId, senderType)));
    }

    @PatchMapping("/conversation/{conversationId}/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @PathVariable Long conversationId,
            @RequestHeader("Authorization") String token) {
        String senderType = getSenderType(token);
        messagingService.markAllAsRead(conversationId, senderType);
        return ResponseEntity.ok(ApiResponse.success("All messages marked as read", null));
    }

    private Long getCurrentUserId(String token) {
        // TODO: Extract userId from JWT token
        return 1L;
    }

    private Long getProviderId(String token) {
        // TODO: Extract providerId from JWT token
        return 1L;
    }

    private String getSenderType(String token) {
        // TODO: Extract user role from JWT token
        return "TOURIST";
    }
}