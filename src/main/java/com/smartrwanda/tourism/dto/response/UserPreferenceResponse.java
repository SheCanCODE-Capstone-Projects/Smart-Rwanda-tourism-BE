package com.smartrwanda.tourism.dto.response;

import com.smartrwanda.tourism.enums.Language;
import com.smartrwanda.tourism.enums.NotificationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceResponse {
    private Language language;
    private NotificationType notificationType;
    private boolean emailNotifications;
    private boolean pushNotifications;
    private boolean smsNotifications;
    private boolean twoFactorAuth;
    private boolean publicProfile;
}