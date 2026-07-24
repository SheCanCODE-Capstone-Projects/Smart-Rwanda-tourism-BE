package com.smartrwanda.tourism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_reset_tokens")
@Getter
@Setter
@NoArgsConstructor
public class OtpResetToken extends BaseEntity {

    @Column(nullable = false, length = 6)
    private String otp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "is_used")
    private Boolean isUsed = false;

    @Column(name = "is_verified")
    private Boolean isVerified = false;
}
