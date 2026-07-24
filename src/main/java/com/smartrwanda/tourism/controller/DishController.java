package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.DishRequest;
import com.smartrwanda.tourism.dto.DishResponse;
import com.smartrwanda.tourism.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @PostMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<DishResponse>> create(
            @PathVariable Long providerId, @Valid @RequestBody DishRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Dish added", dishService.create(providerId, request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DishResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(dishService.getById(id)));
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<List<DishResponse>>> getByProvider(@PathVariable Long providerId) {
        return ResponseEntity.ok(ApiResponse.success(dishService.getByProvider(providerId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DishResponse>> update(
            @PathVariable Long id, @Valid @RequestBody DishRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Dish updated", dishService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        dishService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Dish deleted", null));
    }
}