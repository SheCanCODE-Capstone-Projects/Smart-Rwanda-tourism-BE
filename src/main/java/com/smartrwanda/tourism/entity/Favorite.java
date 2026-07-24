package com.smartrwanda.tourism.entity;

import com.smartrwanda.tourism.enums.ReviewTargetType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "favorites", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "target_type", "target_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Favorite extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private ReviewTargetType targetType;

    @Column(name = "target_id", nullable = false)
    private Long targetId;
}
