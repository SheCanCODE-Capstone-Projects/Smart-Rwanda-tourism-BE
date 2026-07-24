package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.AdminProviderResponse;
import com.smartrwanda.tourism.dto.AdminUserResponse;
import com.smartrwanda.tourism.dto.DashboardStatistics;
import com.smartrwanda.tourism.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;



    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<DashboardStatistics>> getDashboardStatistics() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getDashboardStatistics()));
    }



    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<AdminUserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getAllUsers()));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<AdminUserResponse>> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(adminService.getUserById(userId)));
    }

    @PatchMapping("/users/{userId}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable Long userId) {
        adminService.deactivateUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User deactivated successfully", null));
    }

    @PatchMapping("/users/{userId}/activate")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable Long userId) {
        adminService.activateUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User activated successfully", null));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }


    @GetMapping("/providers")
    public ResponseEntity<ApiResponse<List<AdminProviderResponse>>> getAllProviders() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getAllProviders()));
    }

    @GetMapping("/providers/{providerId}")
    public ResponseEntity<ApiResponse<AdminProviderResponse>> getProviderById(@PathVariable Long providerId) {
        return ResponseEntity.ok(ApiResponse.success(adminService.getProviderById(providerId)));
    }

    @PatchMapping("/providers/{providerId}/verify")
    public ResponseEntity<ApiResponse<Void>> verifyProvider(@PathVariable Long providerId) {
        adminService.verifyProvider(providerId);
        return ResponseEntity.ok(ApiResponse.success("Provider verified successfully", null));
    }

    @PatchMapping("/providers/{providerId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectProvider(
            @PathVariable Long providerId,
            @RequestParam String reason) {
        adminService.rejectProvider(providerId, reason);
        return ResponseEntity.ok(ApiResponse.success("Provider rejected", null));
    }

    @DeleteMapping("/providers/{providerId}")
    public ResponseEntity<ApiResponse<Void>> deleteProvider(@PathVariable Long providerId) {
        adminService.deleteProvider(providerId);
        return ResponseEntity.ok(ApiResponse.success("Provider deleted successfully", null));
    }
}