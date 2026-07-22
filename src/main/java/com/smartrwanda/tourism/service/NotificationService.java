package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.NotificationResponse;
import com.smartrwanda.tourism.entity.Reservation;

import java.util.List;

public interface NotificationService {


    void sendBookingRequestNotification(Reservation reservation);

    void sendBookingConfirmedNotification(Reservation reservation);

    void sendBookingRejectedNotification(Reservation reservation, String reason);

    void sendBookingCancelledNotification(Reservation reservation, String reason);

    void sendNewMessageNotification(Long userId, Long providerId, String message, Long conversationId);


    List<NotificationResponse> getProviderNotifications(Long providerId);

    List<NotificationResponse> getUserNotifications(Long userId);

    List<NotificationResponse> getProviderUnreadNotifications(Long providerId);

    List<NotificationResponse> getUserUnreadNotifications(Long userId);

    long getProviderUnreadCount(Long providerId);

    long getUserUnreadCount(Long userId);


    void markAsRead(Long notificationId);

    void markAllProviderRead(Long providerId);

    void markAllUserRead(Long userId);


    void deleteNotification(Long notificationId);
}