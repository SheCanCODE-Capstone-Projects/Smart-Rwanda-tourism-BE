package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.request.FavoriteRequest;
import com.smartrwanda.tourism.dto.response.FavoriteResponse;
import com.smartrwanda.tourism.enums.ReviewTargetType;
import com.smartrwanda.tourism.service.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<ApiResponse<FavoriteResponse>> addFavorite(
            @Valid @RequestBody FavoriteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Added to favorites", favoriteService.addFavorite(request)));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> removeFavorite(
            @RequestParam ReviewTargetType targetType,
            @RequestParam Long targetId) {
        favoriteService.removeFavorite(targetType, targetId);
        return ResponseEntity.ok(ApiResponse.success("Removed from favorites", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FavoriteResponse>>> getMyFavorites() {
        return ResponseEntity.ok(ApiResponse.success(favoriteService.getMyFavorites()));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<FavoriteResponse>>> getMyFavoritesByType(
            @RequestParam ReviewTargetType targetType) {
        return ResponseEntity.ok(ApiResponse.success(favoriteService.getMyFavoritesByType(targetType)));
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> isFavorited(
            @RequestParam ReviewTargetType targetType,
            @RequestParam Long targetId) {
        return ResponseEntity.ok(ApiResponse.success(favoriteService.isFavorited(targetType, targetId)));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countMyFavorites() {
        return ResponseEntity.ok(ApiResponse.success(favoriteService.countMyFavorites()));
    }
}
