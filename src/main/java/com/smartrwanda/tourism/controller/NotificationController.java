package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.response.NotificationResponse;
import com.smartrwanda.tourism.entity.User;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.repository.ProviderRepository;
import com.smartrwanda.tourism.repository.UserRepository;
import com.smartrwanda.tourism.security.JwtService;
import com.smartrwanda.tourism.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;

    @GetMapping("/provider")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getProviderNotifications(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(
                notificationService.getProviderNotifications(getProviderId(token))));
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUserNotifications(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(
                notificationService.getUserNotifications(getCurrentUserId(token))));
    }

    @GetMapping("/provider/unread")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getProviderUnread(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(
                notificationService.getProviderUnreadNotifications(getProviderId(token))));
    }

    @GetMapping("/user/unread")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUserUnread(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(
                notificationService.getUserUnreadNotifications(getCurrentUserId(token))));
    }

    @GetMapping("/provider/unread-count")
    public ResponseEntity<ApiResponse<Long>> getProviderUnreadCount(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(
                notificationService.getProviderUnreadCount(getProviderId(token))));
    }

    @GetMapping("/user/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUserUnreadCount(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(ApiResponse.success(
                notificationService.getUserUnreadCount(getCurrentUserId(token))));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(ApiResponse.<Void>success("Marked as read", null));
    }

    @PatchMapping("/provider/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllProviderRead(
            @RequestHeader("Authorization") String token) {
        notificationService.markAllProviderRead(getProviderId(token));
        return ResponseEntity.ok(ApiResponse.<Void>success("All marked as read", null));
    }

    @PatchMapping("/user/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllUserRead(
            @RequestHeader("Authorization") String token) {
        notificationService.markAllUserRead(getCurrentUserId(token));
        return ResponseEntity.ok(ApiResponse.<Void>success("All marked as read", null));
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(ApiResponse.<Void>success("Notification deleted", null));
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
}
