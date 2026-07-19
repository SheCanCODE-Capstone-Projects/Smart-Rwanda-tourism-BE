package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.ReservationRequest;
import com.smartrwanda.tourism.dto.ReservationResponse;
import com.smartrwanda.tourism.entity.ReservationStatus;
import com.smartrwanda.tourism.service.UserReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class UserReservationController {

    private final UserReservationService userReservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> createReservation(
            @Valid @RequestBody ReservationRequest request,
            @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Reservation request sent successfully",
                        userReservationService.createReservation(request, userId)));
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getUserReservations(
            @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        return ResponseEntity.ok(ApiResponse.success(userReservationService.getUserReservations(userId)));
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationResponse>> getReservationById(
            @PathVariable Long reservationId,
            @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        return ResponseEntity.ok(ApiResponse.success(userReservationService.getReservationById(reservationId, userId)));
    }

    @PatchMapping("/{reservationId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelReservation(
            @PathVariable Long reservationId,
            @RequestParam(required = false) String reason,
            @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        userReservationService.cancelReservation(reservationId, userId, reason);
        return ResponseEntity.ok(ApiResponse.success("Reservation cancelled successfully", null));
    }

    @GetMapping("/user/status/{status}")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getUserReservationsByStatus(
            @PathVariable ReservationStatus status,
            @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        return ResponseEntity.ok(ApiResponse.success(
                userReservationService.getUserReservationsByStatus(userId, status)));
    }

    @GetMapping("/user/count/{status}")
    public ResponseEntity<ApiResponse<Long>> countReservationsByStatus(
            @PathVariable ReservationStatus status,
            @RequestHeader("Authorization") String token) {
        Long userId = getCurrentUserId(token);
        return ResponseEntity.ok(ApiResponse.success(
                userReservationService.countReservationsByStatus(userId, status)));
    }

    private Long getCurrentUserId(String token) {
        // TODO: Extract userId from JWT token
        return 1L;
    }
}