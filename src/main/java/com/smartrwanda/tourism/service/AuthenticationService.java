package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.*;

public interface AuthenticationService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse googleLogin(GoogleLoginRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    void logout(String token);

    UserResponse getCurrentUser();
}
