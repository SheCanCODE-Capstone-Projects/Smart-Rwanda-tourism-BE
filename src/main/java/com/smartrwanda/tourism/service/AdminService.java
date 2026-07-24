package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.AdminAttractionUpdateRequest;
import com.smartrwanda.tourism.dto.AdminAttractionResponse;
import com.smartrwanda.tourism.dto.AdminProviderResponse;
import com.smartrwanda.tourism.dto.AdminUserResponse;
import com.smartrwanda.tourism.dto.DashboardStatistics;

import java.util.List;

public interface AdminService {

    DashboardStatistics getDashboardStatistics();

    List<AdminUserResponse> getAllUsers();

    AdminUserResponse getUserById(Long userId);

    void deactivateUser(Long userId);

    void activateUser(Long userId);

    void deleteUser(Long userId);

    List<AdminProviderResponse> getAllProviders();

    AdminProviderResponse getProviderById(Long providerId);

    void verifyProvider(Long providerId);

    void rejectProvider(Long providerId, String reason);

    void deleteProvider(Long providerId);

    List<AdminAttractionResponse> getAllAttractions();

    AdminAttractionResponse getAttractionById(Long attractionId);

    AdminAttractionResponse updateAttraction(Long attractionId, AdminAttractionUpdateRequest request);

    void deleteAttraction(Long attractionId);
}