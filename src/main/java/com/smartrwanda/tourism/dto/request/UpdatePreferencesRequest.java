package com.smartrwanda.tourism.dto.request;
import com.smartrwanda.tourism.enums.Language;
import com.smartrwanda.tourism.enums.NotificationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePreferencesRequest {
    private Language language;
    private NotificationType notificationType;
    private Boolean emailNotifications;
    private Boolean pushNotifications;
    private Boolean smsNotifications;
    private Boolean twoFactorAuth;
    private Boolean publicProfile;
}