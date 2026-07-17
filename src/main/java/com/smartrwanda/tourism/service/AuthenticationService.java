package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.AuthResponse;
import com.smartrwanda.tourism.dto.ForgotPasswordRequest;
import com.smartrwanda.tourism.dto.GoogleLoginRequest;
import com.smartrwanda.tourism.dto.LoginRequest;
import com.smartrwanda.tourism.dto.RegisterRequest;
import com.smartrwanda.tourism.dto.ResetPasswordRequest;
import com.smartrwanda.tourism.dto.UserResponse;

public interface AuthenticationService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse googleLogin(GoogleLoginRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    void logout(String token);

    UserResponse getCurrentUser();
}