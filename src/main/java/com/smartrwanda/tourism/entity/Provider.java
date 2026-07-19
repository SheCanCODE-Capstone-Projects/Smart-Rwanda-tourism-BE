package com.smartrwanda.tourism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "providers")
@Getter
@Setter
@NoArgsConstructor
public class Provider extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProviderCategory category;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String phone;

    private String email;

    private String location;

    @ElementCollection
    @CollectionTable(name = "provider_images", joinColumns = @JoinColumn(name = "provider_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;
}
