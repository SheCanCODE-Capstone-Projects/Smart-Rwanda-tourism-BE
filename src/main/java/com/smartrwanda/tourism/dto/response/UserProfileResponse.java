package com.smartrwanda.tourism.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Long userId;
    private String username;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String bio;
    private String dateOfBirth;
    private String address;
    private String city;
    private String country;
    private String profilePictureUrl;
    private boolean accountEnabled;
    private String lastLoginDate;
}

