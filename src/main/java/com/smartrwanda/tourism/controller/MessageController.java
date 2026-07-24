package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.request.MessageRequest;
import com.smartrwanda.tourism.dto.response.ConversationResponse;
import com.smartrwanda.tourism.dto.response.MessageResponse;
import com.smartrwanda.tourism.entity.Role;
import com.smartrwanda.tourism.entity.User;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.repository.ProviderRepository;
import com.smartrwanda.tourism.repository.UserRepository;
import com.smartrwanda.tourism.security.JwtService;
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
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;

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
        return ResponseEntity.ok(ApiResponse.<Void>success("All messages marked as read", null));
    }

    private User resolveUser(String token) {
        String bearer = token.startsWith("Bearer ") ? token.substring(7) : token;
        String email = jwtService.extractEmail(bearer);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Long getCurrentUserId(String token) {
        return resolveUser(token).getId();
    }

    private Long getProviderId(String token) {
        User user = resolveUser(token);
        return providerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Provider profile not found for this user"))
                .getId();
    }

    private String getSenderType(String token) {
        User user = resolveUser(token);
        return user.getRole() == Role.PROVIDER ? "PROVIDER" : "TOURIST";
    }
}
