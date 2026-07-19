package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.ProviderRequest;
import com.smartrwanda.tourism.dto.ProviderResponse;
import com.smartrwanda.tourism.dto.ProviderStatistics;
import com.smartrwanda.tourism.dto.ProviderSummaryResponse;
import com.smartrwanda.tourism.entity.Provider;
import com.smartrwanda.tourism.entity.ProviderCategory;
import org.springframework.transaction.annotation.Transactional;
import com.smartrwanda.tourism.entity.User;
import com.smartrwanda.tourism.entity.VerificationStatus;
import com.smartrwanda.tourism.exception.BadRequestException;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.mapper.ProviderMapper;
import com.smartrwanda.tourism.repository.ProviderRepository;
import com.smartrwanda.tourism.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;
    private final ProviderMapper providerMapper;

    @Override
    public ProviderResponse registerProvider(Long userId, ProviderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (providerRepository.findByUserId(userId).isPresent()) {
            throw new BadRequestException("This user already has a provider profile");
        }

        com.smartrwanda.tourism.entity.Provider provider = providerMapper.toEntity(request);
        provider.setUser(user);
        provider.setVerificationStatus(VerificationStatus.PENDING);

        return providerMapper.toResponse(providerRepository.save(provider));
    }

    @Override
    public ProviderResponse getById(Long id) {
        com.smartrwanda.tourism.entity.Provider provider = providerRepository.findById(id)
                .orElseThrow();
        return providerMapper.toResponse(provider);
    }

    @Override
    public List<ProviderSummaryResponse> getAll() {
        return providerRepository.findAll().stream()
                .map(providerMapper::toSummaryResponse)
                .toList();
    }


    @Override
    public List<ProviderSummaryResponse> getByCategory(ProviderCategory category) {
        return providerRepository.findByCategory(category).stream()
                .map(providerMapper::toSummaryResponse)
                .toList();
    }

    @Override
    public ProviderResponse update(Long id, ProviderRequest request) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));

        provider.setBusinessName(request.getBusinessName());
        provider.setCategory(request.getCategory());
        provider.setDescription(request.getDescription());
        provider.setPhone(request.getPhone());
        provider.setEmail(request.getEmail());
        provider.setLocation(request.getLocation());
        provider.setImageUrls(request.getImageUrls());

        return providerMapper.toResponse(providerRepository.save(provider));
    }

    @Override
    public void delete(Long id) {
        if (!providerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Provider not found");
        }
        providerRepository.deleteById(id);
    }

    @Override
    public List<ProviderSummaryResponse> getPendingProviders() {
        return providerRepository.findByVerificationStatus(VerificationStatus.PENDING).stream()
                .map(providerMapper::toSummaryResponse)
                .toList();
    }

    @Override
    public ProviderResponse verifyProvider(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        provider.setVerificationStatus(VerificationStatus.VERIFIED);
        return providerMapper.toResponse(providerRepository.save(provider));
    }

    @Override
    public ProviderResponse rejectProvider(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        provider.setVerificationStatus(VerificationStatus.REJECTED);
        return providerMapper.toResponse(providerRepository.save(provider));
    }

    @Override
    public ProviderStatistics getStatistics() {
        return ProviderStatistics.builder()
                .totalProviders(providerRepository.count())
                .pendingCount(providerRepository.countByVerificationStatus(VerificationStatus.PENDING))
                .verifiedCount(providerRepository.countByVerificationStatus(VerificationStatus.VERIFIED))
                .rejectedCount(providerRepository.countByVerificationStatus(VerificationStatus.REJECTED))
                .build();
    }
}
