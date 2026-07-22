package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.ProviderRequest;
import com.smartrwanda.tourism.dto.ProviderResponse;
import com.smartrwanda.tourism.dto.ProviderStatistics;
import com.smartrwanda.tourism.dto.ProviderSummaryResponse;
import com.smartrwanda.tourism.entity.Provider;
import com.smartrwanda.tourism.entity.ProviderCategory;
import com.smartrwanda.tourism.entity.User;
import com.smartrwanda.tourism.entity.VerificationStatus;
import com.smartrwanda.tourism.exception.BadRequestException;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.mapper.ProviderMapper;
import com.smartrwanda.tourism.repository.ProviderRepository;
import com.smartrwanda.tourism.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

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

        Provider provider = providerMapper.toEntity(request);
        provider.setUser(user);
        provider.setVerificationStatus(VerificationStatus.PENDING);

        return providerMapper.toResponse(providerRepository.save(provider));
    }

    @Override
    public ProviderResponse getById(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        return providerMapper.toResponse(provider);
    }

    @Override
    public List<ProviderSummaryResponse> getAll() {
        return providerRepository.findAll().stream()
                .map(providerMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProviderSummaryResponse> getByCategory(ProviderCategory category) {
        return providerRepository.findByCategory(category).stream()
                .map(providerMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProviderResponse update(Long id, ProviderRequest request) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));

        provider.setBusinessName(request.getBusinessName());
        provider.setCategory(request.getCategory());
        provider.setDescription(request.getDescription());
        provider.setContactPhone(request.getPhone());
        provider.setContactEmail(request.getEmail());
        provider.setLocation(request.getLocation());

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
    public List<ProviderResponse> getAllProviders() {
        return providerRepository.findAll().stream()
                .map(providerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProviderResponse> getProvidersByCategory(ProviderCategory category) {
        return providerRepository.findByCategory(category).stream()
                .map(providerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProviderResponse> getVerifiedProviders() {
        return providerRepository.findByVerificationStatus(VerificationStatus.VERIFIED).stream()
                .map(providerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProviderResponse> getProvidersByUser(Long userId) {
        // findByUserId returns Optional<Provider> — map to list of 0 or 1 element
        return providerRepository.findByUserId(userId)
                .map(providerMapper::toResponse)
                .map(List::of)
                .orElse(List.of());
    }

    @Override
    public List<ProviderResponse> searchProviders(String keyword) {
        return providerRepository.findByBusinessNameContainingIgnoreCase(keyword).stream()
                .map(providerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProviderResponse> getProvidersByLocation(String location) {
        return providerRepository.findByLocationContainingIgnoreCase(location).stream()
                .map(providerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProviderResponse uploadLogo(Long id, MultipartFile file) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        return providerMapper.toResponse(providerRepository.save(provider));
    }

    @Override
    public ProviderResponse uploadCoverImage(Long id, MultipartFile file) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        return providerMapper.toResponse(providerRepository.save(provider));
    }

    @Override
    public void deleteLogo(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        provider.setLogoUrl(null);
        providerRepository.save(provider);
    }

    @Override
    public void deleteCoverImage(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        provider.setCoverImageUrl(null);
        providerRepository.save(provider);
    }

    @Override
    public List<ProviderSummaryResponse> getPendingProviders() {
        return providerRepository.findByVerificationStatus(VerificationStatus.PENDING).stream()
                .map(providerMapper::toSummaryResponse)
                .collect(Collectors.toList());
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

    @Override
    public void verifyProvider(Long id, String status) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        provider.setVerificationStatus(VerificationStatus.valueOf(status));
        providerRepository.save(provider);
    }

    @Override
    public void approveProvider(Long id) {
        verifyProvider(id, "VERIFIED");
    }

    @Override
    public void rejectProvider(Long id, String reason) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        provider.setVerificationStatus(VerificationStatus.REJECTED);
        providerRepository.save(provider);
    }

    @Override
    public ProviderStatistics getProviderStatistics(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));

        return ProviderStatistics.builder()
                .totalProviders(1L)
                .pendingCount(provider.getVerificationStatus() == VerificationStatus.PENDING ? 1L : 0L)
                .verifiedCount(provider.getVerificationStatus() == VerificationStatus.VERIFIED ? 1L : 0L)
                .rejectedCount(provider.getVerificationStatus() == VerificationStatus.REJECTED ? 1L : 0L)
                .build();
    }

    @Override
    public long getTotalProvidersCount() {
        return providerRepository.count();
    }

    @Override
    public long getVerifiedProvidersCount() {
        return providerRepository.countByVerificationStatus(VerificationStatus.VERIFIED);
    }

    @Override
    public long getPendingProvidersCount() {
        return providerRepository.countByVerificationStatus(VerificationStatus.PENDING);
    }

    @Override
    public List<ProviderSummaryResponse> getAllProviderSummaries() {
        return providerRepository.findAll().stream()
                .map(providerMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProviderResponse getProviderById(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        return providerMapper.toResponse(provider);
    }
}