package com.smartrwanda.tourism.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.smartrwanda.tourism.dto.request.*;
import com.smartrwanda.tourism.dto.response.AuthResponse;
import com.smartrwanda.tourism.dto.UserResponse;
import com.smartrwanda.tourism.entity.*;
import com.smartrwanda.tourism.exception.BadRequestException;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.repository.*;
import com.smartrwanda.tourism.security.JwtService;
import com.smartrwanda.tourism.validator.EmailValidator;
import com.smartrwanda.tourism.validator.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final OtpResetTokenRepository otpResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private final JavaMailSender mailSender;

    @Value("${google.client-id:}")
    private String googleClientId;

    @Value("${google.client-secret:}")
    private String googleClientSecret;

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
        return buildAuthResponse(token, savedUser);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (user.getAuthProvider() == AuthProvider.GOOGLE) {
            throw new BadRequestException("This account uses Google login. Please sign in with Google.");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);
        return buildAuthResponse(token, user);
    }

    @Override
    @Transactional
    public AuthResponse googleLogin(GoogleLoginRequest request) {
        GoogleIdToken.Payload payload = verifyGoogleToken(request.getIdToken());

        String email = payload.getEmail();
        String firstName = (String) payload.get("given_name");
        String lastName = (String) payload.get("family_name");
        String picture = (String) payload.get("picture");

        if (firstName == null) firstName = email.split("@")[0];
        if (lastName == null) lastName = "";

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            user.setRole(Role.TOURIST);
            user.setAuthProvider(AuthProvider.GOOGLE);
            user.setProfilePicture(picture);
            user = userRepository.save(user);

            UserProfile profile = new UserProfile();
            profile.setUserId(user.getId());
            profile.setFirstName(user.getFirstName());
            profile.setLastName(user.getLastName());
            userProfileRepository.save(profile);

            UserPreference preferences = new UserPreference();
            preferences.setUserId(user.getId());
            userPreferenceRepository.save(preferences);

            sendWelcomeEmail(user.getEmail(), user.getFirstName());

        } else if (user.getAuthProvider() != AuthProvider.GOOGLE) {
            throw new BadRequestException(
                    "An account with this email already exists. Please log in with email and password.");
        } else {
            user.setProfilePicture(picture);
            userRepository.save(user);
        }

        String token = jwtService.generateToken(user);
        return buildAuthResponse(token, user);
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No account found with that email address"));

        otpResetTokenRepository.deleteByUser(user);

        String otp = generateOtp();

        OtpResetToken otpToken = new OtpResetToken();
        otpToken.setOtp(otp);
        otpToken.setUser(user);
        otpToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        otpToken.setIsUsed(false);
        otpToken.setIsVerified(false);
        otpResetTokenRepository.save(otpToken);

        sendOtpEmail(user.getEmail(), user.getFirstName(), otp);
    }

    @Override
    @Transactional
    public void verifyOtp(VerifyOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or OTP"));

        OtpResetToken otpToken = otpResetTokenRepository
                .findByUserAndOtpAndIsUsedFalse(user, request.getOtp())
                .orElseThrow(() -> new BadRequestException("Invalid or expired OTP"));

        if (otpToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP has expired. Please request a new one.");
        }

        otpToken.setIsVerified(true);
        otpResetTokenRepository.save(otpToken);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        if (!passwordValidator.isValid(request.getNewPassword())) {
            throw new BadRequestException("Password does not meet security requirements");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or OTP"));

        OtpResetToken otpToken = otpResetTokenRepository
                .findByUserAndOtpAndIsUsedFalse(user, request.getOtp())
                .orElseThrow(() -> new BadRequestException("Invalid or expired OTP"));

        if (otpToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP has expired. Please request a new one.");
        }

        if (!Boolean.TRUE.equals(otpToken.getIsVerified())) {
            throw new BadRequestException("OTP has not been verified. Please call /verify-otp first.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        otpToken.setIsUsed(true);
        otpResetTokenRepository.save(otpToken);
    }

    @Override
    public void logout(String token) {
    }

    @Override
    public UserResponse getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private AuthResponse buildAuthResponse(String token, User user) {
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(1_000_000));
    }

    private GoogleIdToken.Payload verifyGoogleToken(String idTokenString) {
        if (googleClientId == null || googleClientId.isBlank()) {
            throw new BadRequestException("Google login is not configured on this server.");
        }
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new BadRequestException("Invalid Google token. Please try again.");
            }
            return idToken.getPayload();
        } catch (GeneralSecurityException | IOException e) {
            throw new BadRequestException("Failed to verify Google token: " + e.getMessage());
        }
    }

    private void sendOtpEmail(String to, String firstName, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Your Password Reset OTP - RwandaWays");
            message.setText(
                    "Hello " + firstName + ",\n\n" +
                    "We received a request to reset your RwandaWays password.\n\n" +
                    "Your One-Time Password (OTP) is:\n\n" +
                    "    " + otp + "\n\n" +
                    "This OTP is valid for 10 minutes. Do not share it with anyone.\n\n" +
                    "If you did not request a password reset, please ignore this email.\n\n" +
                    "Best regards,\n" +
                    "RwandaWays Team"
            );
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send OTP email to " + to + ": " + e.getMessage());
        }
    }

    private void sendWelcomeEmail(String to, String firstName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Welcome to RwandaWays!");
            message.setText(
                    "Hello " + firstName + ",\n\n" +
                    "Thank you for registering with RwandaWays!\n\n" +
                    "We're excited to have you on board. With your account, you can:\n" +
                    "- Discover amazing tourist attractions in Rwanda\n" +
                    "- Book hotels, tours, and activities\n" +
                    "- Plan your perfect trip\n" +
                    "- Connect with verified service providers\n\n" +
                    "Start exploring: http://localhost:3000\n\n" +
                    "Best regards,\n" +
                    "RwandaWays Team"
            );
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send welcome email to " + to + ": " + e.getMessage());
        }
    }
}
