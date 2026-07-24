package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.FavoriteRequest;
import com.smartrwanda.tourism.dto.FavoriteResponse;
import com.smartrwanda.tourism.entity.Favorite;
import com.smartrwanda.tourism.entity.User;
import com.smartrwanda.tourism.enums.ReviewTargetType;
import com.smartrwanda.tourism.exception.ResourceNotFoundException;
import com.smartrwanda.tourism.repository.FavoriteRepository;
import com.smartrwanda.tourism.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    @Override
    public FavoriteResponse addFavorite(FavoriteRequest request) {
        User currentUser = getCurrentUser();

        if (favoriteRepository.existsByUserIdAndTargetTypeAndTargetId(
                currentUser.getId(), request.getTargetType(), request.getTargetId())) {
            throw new IllegalStateException("This item is already in your favorites");
        }
        Favorite favorite = new Favorite();
        favorite.setUser(currentUser);
        favorite.setTargetType(request.getTargetType());
        favorite.setTargetId(request.getTargetId());
        Favorite saved = favoriteRepository.save(favorite);
        return mapToResponse(saved);
    }
    @Override
    @Transactional
    public void removeFavorite(ReviewTargetType targetType, Long targetId) {
        User currentUser = getCurrentUser();

        if (!favoriteRepository.existsByUserIdAndTargetTypeAndTargetId(
                currentUser.getId(), targetType, targetId)) {
            throw new ResourceNotFoundException("Favorite not found");
        }

        favoriteRepository.deleteByUserIdAndTargetTypeAndTargetId(
                currentUser.getId(), targetType, targetId);
    }

    @Override
    public List<FavoriteResponse> getMyFavorites() {
        User currentUser = getCurrentUser();
        return favoriteRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<FavoriteResponse> getMyFavoritesByType(ReviewTargetType targetType) {
        User currentUser = getCurrentUser();
        return favoriteRepository.findByUserIdAndTargetType(currentUser.getId(), targetType)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public boolean isFavorited(ReviewTargetType targetType, Long targetId) {
        User currentUser = getCurrentUser();
        return favoriteRepository.existsByUserIdAndTargetTypeAndTargetId(
                currentUser.getId(), targetType, targetId);
    }

    @Override
    public long countMyFavorites() {
        User currentUser = getCurrentUser();
        return favoriteRepository.countByUserId(currentUser.getId());
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }
    private FavoriteResponse mapToResponse(Favorite favorite) {
        return FavoriteResponse.builder()
                .id(favorite.getId())
                .userId(favorite.getUser().getId())
                .userName(favorite.getUser().getFirstName() + " " + favorite.getUser().getLastName())
                .targetType(favorite.getTargetType())
                .targetId(favorite.getTargetId())
                .createdAt(favorite.getCreatedAt())
                .build();
    }
}
