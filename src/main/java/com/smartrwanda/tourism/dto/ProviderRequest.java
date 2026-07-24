package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.entity.ProviderCategory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
public class ProviderRequest {
    @NotBlank
    private String businessName;

    @NotNull
    private ProviderCategory category;

    private String description;
    private String phone;

    @Email
    private String email;

    private String location;
    private List<String> imageUrls;

    private Integer yearsOfExperience;
    private List<String> skills;
    private String profilePictureUrl;
    private Long employerId; // optional, only for Tour Guides working for an agency
}