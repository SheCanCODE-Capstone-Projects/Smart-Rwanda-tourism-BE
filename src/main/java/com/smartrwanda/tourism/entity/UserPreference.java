package com.smartrwanda.tourism.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.smartrwanda.tourism.enums.Language;
import com.smartrwanda.tourism.enums.NotificationType;

@Entity
@Table(name = "user_preferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;  // ← Changed from User to Long

    @Enumerated(EnumType.STRING)
    private Language language = Language.ENGLISH;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType = NotificationType.EMAIL;

    private boolean emailNotifications = true;
    private boolean pushNotifications = true;
    private boolean smsNotifications = false;
    private boolean twoFactorAuth = false;
    private boolean publicProfile = true;
}

