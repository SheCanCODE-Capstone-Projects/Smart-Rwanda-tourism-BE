package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.enums.ReviewTargetType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FavoriteResponse {

    private Long id;
    private Long userId;
    private String userName;
    private ReviewTargetType targetType;
    private Long targetId;
    private LocalDateTime createdAt;
}
