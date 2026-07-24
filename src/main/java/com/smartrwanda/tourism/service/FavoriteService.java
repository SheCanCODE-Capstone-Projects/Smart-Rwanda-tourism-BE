package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.FavoriteRequest;
import com.smartrwanda.tourism.dto.FavoriteResponse;
import com.smartrwanda.tourism.enums.ReviewTargetType;

import java.util.List;

public interface FavoriteService {

    FavoriteResponse addFavorite(FavoriteRequest request);

    void removeFavorite(ReviewTargetType targetType, Long targetId);

    List<FavoriteResponse> getMyFavorites();

    List<FavoriteResponse> getMyFavoritesByType(ReviewTargetType targetType);

    boolean isFavorited(ReviewTargetType targetType, Long targetId);

    long countMyFavorites();
}
