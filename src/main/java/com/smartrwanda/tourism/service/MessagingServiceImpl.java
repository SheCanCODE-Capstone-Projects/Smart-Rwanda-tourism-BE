package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.MessageRequest;
import com.smartrwanda.tourism.dto.ConversationResponse;
import com.smartrwanda.tourism.dto.MessageResponse;
import com.smartrwanda.tourism.entity.*;
import com.smartrwanda.tourism.exception.BadRequestException;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessagingServiceImpl implements MessagingService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public ConversationResponse getOrCreateConversation(Long reservationId) {

        Conversation existingConversation = conversationRepository
                .findByReservationId(reservationId)
                .orElse(null);

        if (existingConversation != null) {
            return mapToConversationResponse(existingConversation);
        }


        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        Conversation conversation = new Conversation();
        conversation.setReservation(reservation);
        conversation.setProvider(reservation.getProvider());
        conversation.setUser(reservation.getUser());
        conversation.setIsActive(true);

        Conversation saved = conversationRepository.save(conversation);
        return mapToConversationResponse(saved);
    }

    @Override
    @Transactional
    public MessageResponse sendMessage(MessageRequest request, Long senderId, String senderType) {
        Conversation conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));


        if ("TOURIST".equals(senderType) && !conversation.getUser().getId().equals(senderId)) {
            throw new BadRequestException("You are not authorized to send messages in this conversation");
        }
        if ("PROVIDER".equals(senderType) && !conversation.getProvider().getId().equals(senderId)) {
            throw new BadRequestException("You are not authorized to send messages in this conversation");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(request.getContent());
        message.setSenderType(senderType);
        message.setIsRead(false);

        Message saved = messageRepository.save(message);


        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);


        if ("TOURIST".equals(senderType)) {

            notificationService.sendNewMessageNotification(
                    conversation.getProvider().getId(),
                    conversation.getProvider().getId(),
                    request.getContent(),
                    conversation.getId()
            );
        } else {

            notificationService.sendNewMessageNotification(
                    conversation.getUser().getId(),
                    conversation.getProvider().getId(),
                    request.getContent(),
                    conversation.getId()
            );
        }

        return mapToMessageResponse(saved);
    }

    @Override
    @Transactional
    public void sendAutoMessage(Long conversationId, String content) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));


        User systemUser = userRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("System user not found"));

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(systemUser);
        message.setContent(content);
        message.setSenderType("SYSTEM");
        message.setIsRead(false);

        messageRepository.save(message);
        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);
    }

    @Override
    public List<MessageResponse> getMessages(Long conversationId) {
        List<Message> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        return messages.stream()
                .map(this::mapToMessageResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConversationResponse> getUserConversations(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        List<Conversation> conversations = conversationRepository.findByUserId(userId);
        return conversations.stream()
                .map(this::mapToConversationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConversationResponse> getProviderConversations(Long providerId) {
        if (!providerRepository.existsById(providerId)) {
            throw new ResourceNotFoundException("Provider not found");
        }

        List<Conversation> conversations = conversationRepository.findByProviderId(providerId);
        return conversations.stream()
                .map(this::mapToConversationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public long getUnreadCount(Long conversationId, String senderType) {
        return messageRepository.countUnreadMessages(conversationId, senderType);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long conversationId, String senderType) {
        messageRepository.markAllAsRead(conversationId, senderType);
    }

    private MessageResponse mapToMessageResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getFirstName() + " " + message.getSender().getLastName())
                .senderType(message.getSenderType())
                .content(message.getContent())
                .isRead(message.getIsRead())
                .createdAt(message.getCreatedAt())
                .build();
    }

    private ConversationResponse mapToConversationResponse(Conversation conversation) {
        List<MessageResponse> messages = conversation.getMessages().stream()
                .map(this::mapToMessageResponse)
                .collect(Collectors.toList());

        long unreadCount = messageRepository.countUnreadMessages(
                conversation.getId(),
                "SYSTEM"
        );

        return ConversationResponse.builder()
                .id(conversation.getId())
                .reservationId(conversation.getReservation().getId())
                .providerId(conversation.getProvider().getId())
                .providerName(conversation.getProvider().getBusinessName())
                .userId(conversation.getUser().getId())
                .userName(conversation.getUser().getFirstName() + " " + conversation.getUser().getLastName())
                .messages(messages)
                .unreadCount(unreadCount)
                .lastMessageAt(conversation.getUpdatedAt())
                .createdAt(conversation.getCreatedAt())
                .build();
    }
}