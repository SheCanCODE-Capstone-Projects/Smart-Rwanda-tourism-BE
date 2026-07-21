package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.ProviderRequest;
import com.smartrwanda.tourism.dto.ProviderResponse;
import com.smartrwanda.tourism.dto.ProviderStatistics;
import com.smartrwanda.tourism.dto.ProviderSummaryResponse;
import com.smartrwanda.tourism.entity.ProviderCategory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProviderService {

    ProviderResponse registerProvider(Long userId, ProviderRequest request);
    ProviderResponse getById(Long id);
    List<ProviderSummaryResponse> getAll();
    List<ProviderSummaryResponse> getByCategory(ProviderCategory category);
    ProviderResponse update(Long id, ProviderRequest request);
    void delete(Long id);


    List<ProviderResponse> getAllProviders();
    List<ProviderResponse> getProvidersByCategory(ProviderCategory category);
    List<ProviderResponse> getVerifiedProviders();
    List<ProviderResponse> getProvidersByUser(Long userId);
    List<ProviderResponse> searchProviders(String keyword);
    List<ProviderResponse> getProvidersByLocation(String location);

    ProviderResponse uploadLogo(Long id, MultipartFile file);
    ProviderResponse uploadCoverImage(Long id, MultipartFile file);
    void deleteLogo(Long id);
    void deleteCoverImage(Long id);


    List<ProviderSummaryResponse> getPendingProviders();
    ProviderResponse verifyProvider(Long id);
    ProviderResponse rejectProvider(Long id);
    ProviderStatistics getStatistics();
    void verifyProvider(Long id, String status);
    void approveProvider(Long id);
    void rejectProvider(Long id, String reason);


    ProviderStatistics getProviderStatistics(Long id);
    long getTotalProvidersCount();
    long getVerifiedProvidersCount();
    long getPendingProvidersCount();
    List<ProviderSummaryResponse> getAllProviderSummaries();
    ProviderResponse getProviderById(Long id);
}
