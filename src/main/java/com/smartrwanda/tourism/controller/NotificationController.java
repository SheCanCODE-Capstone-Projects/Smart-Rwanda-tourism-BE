package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.NotificationResponse;
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

    @GetMapping("/provider")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getProviderNotifications(
            @RequestHeader("Authorization") String token) {
        Long providerId = getProviderId(token);
        return ResponseEntity.ok(ApiResponse.success(
                notificationService.getProviderNotifications(providerId)));
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUserNotifications(
            @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        return ResponseEntity.ok(ApiResponse.success(
                notificationService.getUserNotifications(userId)));
    }

    @GetMapping("/provider/unread")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getProviderUnread(
            @RequestHeader("Authorization") String token) {
        Long providerId = getProviderId(token);
        return ResponseEntity.ok(ApiResponse.success(
                notificationService.getProviderUnreadNotifications(providerId)));
    }

    @GetMapping("/user/unread")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUserUnread(
            @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        return ResponseEntity.ok(ApiResponse.success(
                notificationService.getUserUnreadNotifications(userId)));
    }

    @GetMapping("/provider/unread-count")
    public ResponseEntity<ApiResponse<Long>> getProviderUnreadCount(
            @RequestHeader("Authorization") String token) {
        Long providerId = getProviderId(token);
        return ResponseEntity.ok(ApiResponse.success(
                notificationService.getProviderUnreadCount(providerId)));
    }

    @GetMapping("/user/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUserUnreadCount(
            @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        return ResponseEntity.ok(ApiResponse.success(
                notificationService.getUserUnreadCount(userId)));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(ApiResponse.success("Marked as read", null));
    }

    @PatchMapping("/provider/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllProviderRead(
            @RequestHeader("Authorization") String token) {
        Long providerId = getProviderId(token);
        notificationService.markAllProviderRead(providerId);
        return ResponseEntity.ok(ApiResponse.success("All marked as read", null));
    }

    @PatchMapping("/user/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllUserRead(
            @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        notificationService.markAllUserRead(userId);
        return ResponseEntity.ok(ApiResponse.success("All marked as read", null));
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(ApiResponse.success("Notification deleted", null));
    }

    private Long getCurrentUserId(String token) {
        // TODO: Extract userId from JWT token
        return 1L;
    }

    private Long getProviderId(String token) {
        // TODO: Extract providerId from JWT token
        return 1L;
    }
}