package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.OtpResetToken;
import com.smartrwanda.tourism.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpResetTokenRepository extends JpaRepository<OtpResetToken, Long> {

    Optional<OtpResetToken> findByUserAndOtpAndIsUsedFalse(User user, String otp);

    Optional<OtpResetToken> findTopByUserAndIsUsedFalseOrderByCreatedAtDesc(User user);

    void deleteByUser(User user);
}
