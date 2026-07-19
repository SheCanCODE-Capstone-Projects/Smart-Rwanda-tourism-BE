package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.request.*;
import com.smartrwanda.tourism.dto.response.UserProfileResponse;
import com.smartrwanda.tourism.dto.response.UserPreferenceResponse;
import com.smartrwanda.tourism.dto.response.LoginHistoryResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface UserProfileService {

    UserProfileResponse getUserProfile(Long userId);
    UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request);
    String uploadProfilePicture(Long userId, MultipartFile file);
    void deleteProfilePicture(Long userId);


    void changePassword(Long userId, ChangePasswordRequest request);
    void updateEmail(Long userId, String email);
    void updatePhoneNumber(Long userId, String phoneNumber);


    UserPreferenceResponse getUserPreferences(Long userId);
    UserPreferenceResponse updatePreferences(Long userId, UpdatePreferencesRequest request);


    List<LoginHistoryResponse> getLoginHistory(Long userId);
    List<LoginHistoryResponse> getAccountActivity(Long userId);
    UserProfileResponse getLastLoginInfo(Long userId);


    void deactivateAccount(Long userId, DeactivateAccountRequest request);
    void reactivateAccount(Long userId);
    void deleteAccount(Long userId);

    void logLoginAttempt(Long userId, String ipAddress, String userAgent, boolean successful);
}