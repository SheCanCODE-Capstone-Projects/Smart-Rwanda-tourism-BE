package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.request.*;
import com.smartrwanda.tourism.dto.response.AuthResponse;
import com.smartrwanda.tourism.dto.response.UserResponse;
import com.smartrwanda.tourism.entity.AuthProvider;
import com.smartrwanda.tourism.entity.PasswordResetToken;
import com.smartrwanda.tourism.entity.User;
import com.smartrwanda.tourism.entity.UserProfile;
import com.smartrwanda.tourism.entity.UserPreference;
import com.smartrwanda.tourism.exception.BadRequestException;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.repository.PasswordResetTokenRepository;
import com.smartrwanda.tourism.repository.UserRepository;
import com.smartrwanda.tourism.repository.UserProfileRepository;
import com.smartrwanda.tourism.repository.UserPreferenceRepository;
import com.smartrwanda.tourism.security.JwtService;
import com.smartrwanda.tourism.validator.EmailValidator;
import com.smartrwanda.tourism.validator.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private final JavaMailSender mailSender;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (!emailValidator.isValid(request.getEmail())) {
            throw new BadRequestException("Invalid email format");
        }

        if (!passwordValidator.isValid(request.getPassword())) {
            throw new BadRequestException("Password does not meet security requirements");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setAuthProvider(AuthProvider.LOCAL);

        User savedUser = userRepository.save(user);

        UserProfile profile = new UserProfile();
        profile.setUserId(savedUser.getId());
        profile.setFirstName(savedUser.getFirstName());
        profile.setLastName(savedUser.getLastName());
        userProfileRepository.save(profile);

        UserPreference preferences = new UserPreference();
        preferences.setUserId(savedUser.getId());
        userPreferenceRepository.save(preferences);

        sendWelcomeEmail(savedUser.getEmail(), savedUser.getFirstName());

        String token = jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .token(token)
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (user.getAuthProvider() == AuthProvider.GOOGLE) {
            throw new BadRequestException("Please login using Google");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Override
    public AuthResponse googleLogin(GoogleLoginRequest request) {
        throw new UnsupportedOperationException("Google login not yet implemented");
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        passwordResetTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(24));

        passwordResetTokenRepository.save(resetToken);

        sendPasswordResetEmail(user.getEmail(), token);

        System.out.println("Reset token for " + user.getEmail() + ": " + token);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        if (!passwordValidator.isValid(request.getNewPassword())) {
            throw new BadRequestException("Password does not meet security requirements");
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reset token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }

    @Override
    public void logout(String token) {
        System.out.println("Logout successful for token: " + token.substring(0, Math.min(20, token.length())) + "...");
    }

    @Override
    public UserResponse getCurrentUser() {
        throw new UnsupportedOperationException("Not yet implemented");
    }



    private void sendPasswordResetEmail(String to, String token) {
        try {
            String resetUrl = "http://localhost:8080/api/auth/reset-password?token=" + token;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(" Password Reset Request - RwandaWays");
            message.setText("Hello,\n\n" +
                    "We received a request to reset your password for your RwandaWays account.\n\n" +
                    "Please click the link below to reset your password:\n" +
                    resetUrl + "\n\n" +
                    "This link will expire in 24 hours.\n\n" +
                    "If you did not request this, please ignore this email.\n\n" +
                    "Best regards,\n" +
                    "RwandaWays Team");

            mailSender.send(message);
            System.out.println(" Password reset email sent to: " + to);
        } catch (Exception e) {
            System.out.println(" Failed to send email to: " + to + " - " + e.getMessage());
        }
    }

    private void sendWelcomeEmail(String to, String firstName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(" Welcome to RwandaWays!");
            message.setText("Hello " + firstName + ",\n\n" +
                    "Thank you for registering with RwandaWays! \n\n" +
                    "We're excited to have you on board. With your account, you can:\n" +
                    " Discover amazing tourist attractions in Rwanda\n" +
                    " Book hotels, tours, and activities\n" +
                    " Plan your perfect trip\n" +
                    " Connect with verified service providers\n\n" +
                    "Start exploring now: http://localhost:3000/login\n\n" +
                    "If you have any questions, feel free to reply to this email.\n\n" +
                    "Best regards,\n" +
                    "RwandaWays Team 🌍");

            mailSender.send(message);
            System.out.println(" Welcome email sent to: " + to);
        } catch (Exception e) {
            System.out.println(" Failed to send welcome email to: " + to + " - " + e.getMessage());
        }
    }

}


