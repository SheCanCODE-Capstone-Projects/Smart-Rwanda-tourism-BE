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
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/upload-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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


    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @RequestParam Long userId,
            @Valid @RequestBody ChangePasswordRequest request) {
        userProfileService.changePassword(userId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-email")
    public ResponseEntity<Void> updateEmail(
            @RequestParam Long userId,
            @RequestParam String email) {
        userProfileService.updateEmail(userId, email);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-phone")
    public ResponseEntity<Void> updatePhoneNumber(
            @RequestParam Long userId,
            @RequestParam String phoneNumber) {
        userProfileService.updatePhoneNumber(userId, phoneNumber);
        return ResponseEntity.ok().build();
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

    @GetMapping("/activity")
    public ResponseEntity<List<LoginHistoryResponse>> getAccountActivity(@RequestParam Long userId) {
        return ResponseEntity.ok(userProfileService.getAccountActivity(userId));
    }

    @GetMapping("/last-login")
    public ResponseEntity<UserProfileResponse> getLastLoginInfo(@RequestParam Long userId) {
        return ResponseEntity.ok(userProfileService.getLastLoginInfo(userId));
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

    @PostMapping("/deactivate")
    public ResponseEntity<Void> deactivateAccount(
            @RequestParam Long userId,
            @Valid @RequestBody DeactivateAccountRequest request) {
        userProfileService.deactivateAccount(userId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reactivate")
    public ResponseEntity<Void> reactivateAccount(@RequestParam Long userId) {
        userProfileService.reactivateAccount(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAccount(@RequestParam Long userId) {
        userProfileService.deleteAccount(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

