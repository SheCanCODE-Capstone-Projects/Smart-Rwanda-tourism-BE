package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.response.NotificationResponse;
import com.smartrwanda.tourism.entity.Notification;
import com.smartrwanda.tourism.entity.Reservation;
import com.smartrwanda.tourism.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void sendBookingRequestNotification(Reservation reservation) {

        Notification providerNotification = new Notification();
        providerNotification.setProviderId(reservation.getProvider().getId());  // ✅ SET PROVIDER ID
        providerNotification.setReservation(reservation);
        providerNotification.setTitle(" New Booking Request");
        providerNotification.setMessage(String.format(
                "New booking request from %s for %s",
                reservation.getGuestName(),
                reservation.getProvider().getBusinessName()
        ));
        providerNotification.setType("BOOKING_REQUEST");
        providerNotification.setActionUrl("/provider/reservations/" + reservation.getId());
        providerNotification.setIcon("");
        providerNotification.setTargetType("PROVIDER");
        providerNotification.setIsRead(false);
        notificationRepository.save(providerNotification);

        // Notification for User
        Notification userNotification = new Notification();
        userNotification.setUserId(reservation.getUser().getId());  // ✅ SET USER ID
        userNotification.setReservation(reservation);
        userNotification.setTitle("📋 Booking Request Sent");
        userNotification.setMessage(String.format(
                "Your booking request to %s has been sent. Waiting for confirmation.",
                reservation.getProvider().getBusinessName()
        ));
        userNotification.setType("BOOKING_REQUEST");
        userNotification.setActionUrl("/user/reservations/" + reservation.getId());
        userNotification.setIcon("📋");
        userNotification.setTargetType("USER");
        userNotification.setIsRead(false);
        notificationRepository.save(userNotification);
    }

    @Override
    @Transactional
    public void sendBookingConfirmedNotification(Reservation reservation) {
        // Notification for Provider
        Notification providerNotification = new Notification();
        providerNotification.setProviderId(reservation.getProvider().getId());  // ✅ SET PROVIDER ID
        providerNotification.setReservation(reservation);
        providerNotification.setTitle("✅ Booking Confirmed");
        providerNotification.setMessage(String.format(
                "Booking from %s has been confirmed",
                reservation.getGuestName()
        ));
        providerNotification.setType("BOOKING_CONFIRMED");
        providerNotification.setActionUrl("/provider/reservations/" + reservation.getId());
        providerNotification.setIcon("✅");
        providerNotification.setTargetType("PROVIDER");
        providerNotification.setIsRead(false);
        notificationRepository.save(providerNotification);

        // Notification for User
        Notification userNotification = new Notification();
        userNotification.setUserId(reservation.getUser().getId());  // ✅ SET USER ID
        userNotification.setReservation(reservation);
        userNotification.setTitle("✅ Booking Confirmed!");
        userNotification.setMessage(String.format(
                "Your booking at %s has been confirmed! 🎉",
                reservation.getProvider().getBusinessName()
        ));
        userNotification.setType("BOOKING_CONFIRMED");
        userNotification.setActionUrl("/user/reservations/" + reservation.getId());
        userNotification.setIcon("✅");
        userNotification.setTargetType("USER");
        userNotification.setIsRead(false);
        notificationRepository.save(userNotification);
    }

    @Override
    @Transactional
    public void sendBookingRejectedNotification(Reservation reservation, String reason) {
        // Notification for User
        Notification userNotification = new Notification();
        userNotification.setUserId(reservation.getUser().getId());  // ✅ SET USER ID
        userNotification.setReservation(reservation);
        userNotification.setTitle("❌ Booking Rejected");
        userNotification.setMessage(String.format(
                "Your booking at %s was rejected. Reason: %s",
                reservation.getProvider().getBusinessName(),
                reason != null ? reason : "No reason provided"
        ));
        userNotification.setType("BOOKING_REJECTED");
        userNotification.setActionUrl("/user/reservations/" + reservation.getId());
        userNotification.setIcon("❌");
        userNotification.setTargetType("USER");
        userNotification.setIsRead(false);
        notificationRepository.save(userNotification);
    }

    @Override
    @Transactional
    public void sendBookingCancelledNotification(Reservation reservation, String reason) {
        // Notification for User
        Notification userNotification = new Notification();
        userNotification.setUserId(reservation.getUser().getId());
        userNotification.setReservation(reservation);
        userNotification.setTitle("⚠️ Booking Cancelled");
        userNotification.setMessage(String.format(
                "Your booking at %s has been cancelled. Reason: %s",
                reservation.getProvider().getBusinessName(),
                reason != null ? reason : "No reason provided"
        ));
        userNotification.setType("BOOKING_CANCELLED");
        userNotification.setActionUrl("/user/reservations/" + reservation.getId());
        userNotification.setIcon("⚠️");
        userNotification.setTargetType("USER");
        userNotification.setIsRead(false);
        notificationRepository.save(userNotification);
    }

    @Override
    @Transactional
    public void sendNewMessageNotification(Long userId, Long providerId, String message, Long conversationId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setProviderId(providerId);
        notification.setTitle(" New Message");
        notification.setMessage(message.length() > 100 ? message.substring(0, 100) + "..." : message);
        notification.setType("NEW_MESSAGE");
        notification.setActionUrl("/messages/" + conversationId);
        notification.setIcon(" ");
        notification.setTargetType("USER");
        notification.setIsRead(false);
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponse> getProviderNotifications(Long providerId) {
        return notificationRepository.findByProviderIdOrderByCreatedAtDesc(providerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponse> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponse> getProviderUnreadNotifications(Long providerId) {
        return notificationRepository.findByProviderIdAndIsReadFalseOrderByCreatedAtDesc(providerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponse> getUserUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public long getProviderUnreadCount(Long providerId) {
        return notificationRepository.countByProviderIdAndIsReadFalse(providerId);
    }

    @Override
    public long getUserUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId)
                .ifPresent(notification -> {
                    notification.setIsRead(true);
                    notification.setReadAt(LocalDateTime.now());
                    notificationRepository.save(notification);
                });
    }

    @Override
    @Transactional
    public void markAllProviderRead(Long providerId) {
        notificationRepository.markAllAsReadForProvider(providerId);
    }

    @Override
    @Transactional
    public void markAllUserRead(Long userId) {
        notificationRepository.markAllAsReadForUser(userId);
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .providerId(notification.getProviderId())
                .userId(notification.getUserId())
                .reservationId(notification.getReservation() != null ? notification.getReservation().getId() : null)
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .isRead(notification.getIsRead())
                .actionUrl(notification.getActionUrl())
                .icon(notification.getIcon())
                .targetType(notification.getTargetType())
                .createdAt(notification.getCreatedAt())
                .readAt(notification.getReadAt())
                .build();
    }
}