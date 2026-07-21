package com.smartrwanda.tourism.entity;

import com.smartrwanda.tourism.entity.ProviderCategory;
import com.smartrwanda.tourism.entity.VerificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "providers")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Provider extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    private Provider employer; 

    @Column(name = "business_name", nullable = false)
    private String businessName;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProviderCategory category;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    private String location;

    private String website;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;


    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @ElementCollection
    @CollectionTable(name = "provider_skills", joinColumns = @JoinColumn(name = "provider_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();


    @Column(name = "profile_picture_url")
    private String profilePictureUrl;
}