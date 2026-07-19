package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.Provider;
import com.smartrwanda.tourism.entity.ProviderCategory;
import com.smartrwanda.tourism.entity.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
    Optional<Provider> findByUserId(Long userId);
    List<Provider> findByCategory(ProviderCategory category);
    List<Provider> findByVerificationStatus(VerificationStatus status);
    List<Provider> findByLocationContainingIgnoreCase(String location);
    long countByVerificationStatus(VerificationStatus status);
}