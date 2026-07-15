package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.request.*;
import com.smartrwanda.tourism.dto.response.AuthResponse;

public interface AuthenticationService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    void forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
}