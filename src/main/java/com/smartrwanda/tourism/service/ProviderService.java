package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.ProviderRequest;
import com.smartrwanda.tourism.dto.ProviderResponse;
import com.smartrwanda.tourism.dto.ProviderStatistics;
import com.smartrwanda.tourism.dto.ProviderSummaryResponse;
import com.smartrwanda.tourism.entity.ProviderCategory;

import java.util.List;

public interface ProviderService {
    ProviderResponse registerProvider(Long userId, ProviderRequest request);


    ProviderResponse getById(Long id);
    List<ProviderSummaryResponse> getAll();
    List<ProviderSummaryResponse> getByCategory(ProviderCategory category);
    ProviderResponse update(Long id, ProviderRequest request);
    void delete(Long id);

    // Admin actions
    List<ProviderSummaryResponse> getPendingProviders();
    ProviderResponse verifyProvider(Long id);
    ProviderResponse rejectProvider(Long id);
    ProviderStatistics getStatistics();
}
