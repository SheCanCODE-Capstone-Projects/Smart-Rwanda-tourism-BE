package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.request.ForgotPasswordRequest;
import com.smartrwanda.tourism.dto.request.GoogleLoginRequest;
import com.smartrwanda.tourism.dto.request.LoginRequest;
import com.smartrwanda.tourism.dto.request.RegisterRequest;
import com.smartrwanda.tourism.dto.request.ResetPasswordRequest;
import com.smartrwanda.tourism.dto.request.VerifyOtpRequest;
import com.smartrwanda.tourism.dto.response.AuthResponse;
import com.smartrwanda.tourism.dto.UserResponse;

public interface AuthenticationService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse googleLogin(GoogleLoginRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void verifyOtp(VerifyOtpRequest request);

    void resetPassword(ResetPasswordRequest request);

    void logout(String token);

    UserResponse getCurrentUser();
}
