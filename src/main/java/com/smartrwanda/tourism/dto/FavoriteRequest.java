package com.smartrwanda.tourism.dto;

import com.smartrwanda.tourism.enums.ReviewTargetType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteRequest {

    @NotNull(message = "Target type is required")
    private ReviewTargetType targetType;

    @NotNull(message = "Target id is required")
    private Long targetId;
}
