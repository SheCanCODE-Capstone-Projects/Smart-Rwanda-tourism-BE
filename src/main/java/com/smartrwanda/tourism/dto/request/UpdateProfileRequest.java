package com.smartrwanda.tourism.dto.request;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    private String firstName;
    private String lastName;
    private String bio;
    private String dateOfBirth;
    private String address;
    private String city;
    private String country;
    private String phoneNumber;
    private String profilePictureUrl;
}