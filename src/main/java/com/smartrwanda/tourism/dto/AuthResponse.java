package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponse {
    private String token;
    private String email;
    private Role role;
    private String firstName;
    private String lastName;
}