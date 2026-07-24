package com.smartrwanda.tourism.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;  // ← This field must exist

    private String firstName;
    private String lastName;
    private String bio;
    private LocalDate dateOfBirth;
    private String address;
    private String city;
    private String country;
    private String profilePictureUrl;
}
