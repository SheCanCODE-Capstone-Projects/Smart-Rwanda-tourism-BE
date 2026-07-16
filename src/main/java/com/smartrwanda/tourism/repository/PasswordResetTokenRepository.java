package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.PasswordResetToken;
import com.smartrwanda.tourism.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByTokenAndIsUsedFalse(String token);

    void deleteByUser(User user);

    void deleteByExpiryDateBefore(LocalDateTime dateTime);

    Optional<PasswordResetToken> findByUserAndIsUsedFalse(User user);
}