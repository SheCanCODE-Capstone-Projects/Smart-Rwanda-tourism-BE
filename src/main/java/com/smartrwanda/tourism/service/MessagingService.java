package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.request.MessageRequest;
import com.smartrwanda.tourism.dto.response.ConversationResponse;
import com.smartrwanda.tourism.dto.response.MessageResponse;

import java.util.List;

public interface MessagingService {


    ConversationResponse getOrCreateConversation(Long reservationId);

    MessageResponse sendMessage(MessageRequest request, Long senderId, String senderType);


    void sendAutoMessage(Long conversationId, String content);


    List<MessageResponse> getMessages(Long conversationId);


    List<ConversationResponse> getUserConversations(Long userId);


    List<ConversationResponse> getProviderConversations(Long providerId);


    long getUnreadCount(Long conversationId, String senderType);


    void markAllAsRead(Long conversationId, String senderType);
}