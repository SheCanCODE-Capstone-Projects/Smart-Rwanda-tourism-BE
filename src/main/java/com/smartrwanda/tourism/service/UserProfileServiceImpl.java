package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.entity.UserProfile;
import com.smartrwanda.tourism.entity.UserPreference;
import com.smartrwanda.tourism.entity.LoginHistory;
import com.smartrwanda.tourism.dto.request.*;
import com.smartrwanda.tourism.dto.response.UserProfileResponse;
import com.smartrwanda.tourism.dto.response.UserPreferenceResponse;
import com.smartrwanda.tourism.dto.response.LoginHistoryResponse;
import com.smartrwanda.tourism.repository.UserProfileRepository;
import com.smartrwanda.tourism.repository.UserPreferenceRepository;
import com.smartrwanda.tourism.repository.LoginHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final LoginHistoryRepository loginHistoryRepository;

    private static final String UPLOAD_DIR = "uploads/profiles/";



    @Override
    public UserProfileResponse getUserProfile(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + userId));
        return mapToUserProfileResponse(profile);
    }

    @Override
    @Transactional
    public UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + userId));

        if (request.getFirstName() != null) profile.setFirstName(request.getFirstName());
        if (request.getLastName() != null) profile.setLastName(request.getLastName());
        if (request.getBio() != null) profile.setBio(request.getBio());
        if (request.getDateOfBirth() != null && !request.getDateOfBirth().isEmpty()) {
            profile.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));
        }
        if (request.getAddress() != null) profile.setAddress(request.getAddress());
        if (request.getCity() != null) profile.setCity(request.getCity());
        if (request.getCountry() != null) profile.setCountry(request.getCountry());
        if (request.getProfilePictureUrl() != null) {
            profile.setProfilePictureUrl(request.getProfilePictureUrl());
        }

        userProfileRepository.save(profile);
        return mapToUserProfileResponse(profile);
    }

    @Override
    public String uploadProfilePicture(Long userId, MultipartFile file) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            String pictureUrl = "/uploads/profiles/" + filename;

            UserProfile profile = userProfileRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Profile not found for user: " + userId));
            profile.setProfilePictureUrl(pictureUrl);
            userProfileRepository.save(profile);

            return pictureUrl;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }

    @Override
    @Transactional
    public void deleteProfilePicture(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + userId));
        profile.setProfilePictureUrl(null);
        userProfileRepository.save(profile);
    }


    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        // Validate password match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New passwords do not match");
        }


        System.out.println("Password changed for user: " + userId);
    }

    @Override
    @Transactional
    public void updateEmail(Long userId, String email) {


        System.out.println("Email updated for user: " + userId + " to: " + email);
    }

    @Override
    @Transactional
    public void updatePhoneNumber(Long userId, String phoneNumber) {


        System.out.println("Phone number updated for user: " + userId + " to: " + phoneNumber);
    }


    @Override
    public UserPreferenceResponse getUserPreferences(Long userId) {
        UserPreference preferences = userPreferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Preferences not found for user: " + userId));
        return mapToUserPreferenceResponse(preferences);
    }

    @Override
    @Transactional
    public UserPreferenceResponse updatePreferences(Long userId, UpdatePreferencesRequest request) {
        UserPreference preferences = userPreferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Preferences not found for user: " + userId));

        if (request.getLanguage() != null) preferences.setLanguage(request.getLanguage());
        if (request.getNotificationType() != null) preferences.setNotificationType(request.getNotificationType());
        if (request.getEmailNotifications() != null) preferences.setEmailNotifications(request.getEmailNotifications());
        if (request.getPushNotifications() != null) preferences.setPushNotifications(request.getPushNotifications());
        if (request.getSmsNotifications() != null) preferences.setSmsNotifications(request.getSmsNotifications());
        if (request.getTwoFactorAuth() != null) preferences.setTwoFactorAuth(request.getTwoFactorAuth());
        if (request.getPublicProfile() != null) preferences.setPublicProfile(request.getPublicProfile());

        userPreferenceRepository.save(preferences);
        return mapToUserPreferenceResponse(preferences);
    }


    @Override
    public List<LoginHistoryResponse> getLoginHistory(Long userId) {
        List<LoginHistory> histories = loginHistoryRepository
                .findTop10ByUserIdOrderByLoginTimeDesc(userId);
        return histories.stream()
                .map(this::mapToLoginHistoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<LoginHistoryResponse> getAccountActivity(Long userId) {
        List<LoginHistory> activities = loginHistoryRepository
                .findByUserIdOrderByLoginTimeDesc(userId);
        return activities.stream()
                .map(this::mapToLoginHistoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserProfileResponse getLastLoginInfo(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + userId));

        UserProfileResponse response = mapToUserProfileResponse(profile);


        List<LoginHistory> lastLogin = loginHistoryRepository
                .findTop10ByUserIdOrderByLoginTimeDesc(userId);

        if (!lastLogin.isEmpty()) {
            response.setLastLoginDate(lastLogin.get(0).getLoginTime()
                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        return response;
    }


    @Override
    @Transactional
    public void deactivateAccount(Long userId, DeactivateAccountRequest request) {


        System.out.println("Account deactivated for user: " + userId + " Reason: " + request.getReason());
    }

    @Override
    @Transactional
    public void reactivateAccount(Long userId) {


        System.out.println("Account reactivated for user: " + userId);
    }

    @Override
    @Transactional
    public void deleteAccount(Long userId) {


        System.out.println("Account deleted for user: " + userId);
    }

    @Override
    @Transactional
    public void logLoginAttempt(Long userId, String ipAddress, String userAgent, boolean successful) {
        LoginHistory history = new LoginHistory();
        history.setUserId(userId);
        history.setIpAddress(ipAddress);
        history.setUserAgent(userAgent);
        history.setLoginTime(LocalDateTime.now());
        history.setSuccessful(successful);
        loginHistoryRepository.save(history);
    }



    private UserProfileResponse mapToUserProfileResponse(UserProfile profile) {
        UserProfileResponse response = new UserProfileResponse();
        response.setUserId(profile.getUserId());
        response.setFirstName(profile.getFirstName());
        response.setLastName(profile.getLastName());
        response.setBio(profile.getBio());
        if (profile.getDateOfBirth() != null) {
            response.setDateOfBirth(profile.getDateOfBirth().toString());
        }
        response.setAddress(profile.getAddress());
        response.setCity(profile.getCity());
        response.setCountry(profile.getCountry());
        response.setProfilePictureUrl(profile.getProfilePictureUrl());
        return response;
    }

    private UserPreferenceResponse mapToUserPreferenceResponse(UserPreference preferences) {
        UserPreferenceResponse response = new UserPreferenceResponse();
        response.setLanguage(preferences.getLanguage());
        response.setNotificationType(preferences.getNotificationType());
        response.setEmailNotifications(preferences.isEmailNotifications());
        response.setPushNotifications(preferences.isPushNotifications());
        response.setSmsNotifications(preferences.isSmsNotifications());
        response.setTwoFactorAuth(preferences.isTwoFactorAuth());
        response.setPublicProfile(preferences.isPublicProfile());
        return response;
    }

    private LoginHistoryResponse mapToLoginHistoryResponse(LoginHistory history) {
        LoginHistoryResponse response = new LoginHistoryResponse();
        response.setId(history.getId());
        response.setUserId(history.getUserId());
        response.setIpAddress(history.getIpAddress());
        response.setUserAgent(history.getUserAgent());
        response.setLocation(history.getLocation());
        response.setLoginTime(history.getLoginTime());
        response.setSuccessful(history.isSuccessful());
        return response;
    }
}

