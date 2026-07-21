package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.CarRequest;
import com.smartrwanda.tourism.dto.CarResponse;
import com.smartrwanda.tourism.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<CarResponse>> create(
            @PathVariable Long providerId, @Valid @RequestBody CarRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Car added", carService.create(providerId, request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CarResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(carService.getById(id)));
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<List<CarResponse>>> getByProvider(@PathVariable Long providerId) {
        return ResponseEntity.ok(ApiResponse.success(carService.getByProvider(providerId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CarResponse>> update(
            @PathVariable Long id, @Valid @RequestBody CarRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Car updated", carService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        carService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Car deleted", null));
    }
}
