package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.dto.request.ChangePasswordRequest;
import com.smartrwanda.tourism.dto.request.UpdateProfileRequest;
import com.smartrwanda.tourism.dto.request.UpdatePreferencesRequest;
import com.smartrwanda.tourism.dto.request.DeactivateAccountRequest;
import com.smartrwanda.tourism.dto.response.UserProfileResponse;
import com.smartrwanda.tourism.dto.response.UserPreferenceResponse;
import com.smartrwanda.tourism.dto.response.LoginHistoryResponse;
import com.smartrwanda.tourism.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile(@RequestParam Long userId) {
        return ResponseEntity.ok(userProfileService.getUserProfile(userId));
    }

    @PutMapping
    public ResponseEntity<UserProfileResponse> updateProfile(
            @RequestParam Long userId,
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userProfileService.updateProfile(userId, request));
    }

    @PostMapping("/upload-picture")
    public ResponseEntity<String> uploadProfilePicture(
            @RequestParam Long userId,
            @RequestParam("file") MultipartFile file) {
        String pictureUrl = userProfileService.uploadProfilePicture(userId, file);
        return ResponseEntity.ok(pictureUrl);
    }

    @DeleteMapping("/picture")
    public ResponseEntity<Void> deleteProfilePicture(@RequestParam Long userId) {
        userProfileService.deleteProfilePicture(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/preferences")
    public ResponseEntity<UserPreferenceResponse> getPreferences(@RequestParam Long userId) {
        return ResponseEntity.ok(userProfileService.getUserPreferences(userId));
    }

    @PutMapping("/preferences")
    public ResponseEntity<UserPreferenceResponse> updatePreferences(
            @RequestParam Long userId,
            @Valid @RequestBody UpdatePreferencesRequest request) {
        return ResponseEntity.ok(userProfileService.updatePreferences(userId, request));
    }


    @GetMapping("/history")
    public ResponseEntity<List<LoginHistoryResponse>> getLoginHistory(@RequestParam Long userId) {
        return ResponseEntity.ok(userProfileService.getLoginHistory(userId));
    }


    @PostMapping("/login-attempt")
    public ResponseEntity<Void> logLoginAttempt(
            @RequestParam Long userId,
            @RequestParam String ipAddress,
            @RequestParam String userAgent,
            @RequestParam boolean successful) {
        userProfileService.logLoginAttempt(userId, ipAddress, userAgent, successful);
        return ResponseEntity.ok().build();
    }
}
