package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversationIdOrderByCreatedAtAsc(Long conversationId);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.conversation.id = :conversationId AND m.isRead = false AND m.senderType != :senderType")
    long countUnreadMessages(@Param("conversationId") Long conversationId, @Param("senderType") String senderType);

    @Modifying
    @Query("UPDATE Message m SET m.isRead = true WHERE m.conversation.id = :conversationId AND m.senderType != :senderType")
    void markAllAsRead(@Param("conversationId") Long conversationId, @Param("senderType") String senderType);
}