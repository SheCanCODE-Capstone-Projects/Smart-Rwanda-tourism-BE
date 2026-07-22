package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.dto.ProviderReservationActionRequest;
import com.smartrwanda.tourism.dto.ProviderReservationResponse;
import com.smartrwanda.tourism.dto.ProviderReservationStatistics;
import com.smartrwanda.tourism.entity.ReservationStatus;
import com.smartrwanda.tourism.service.ProviderReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/providers/{providerId}/reservations")
@RequiredArgsConstructor
public class ProviderReservationController {

    private final ProviderReservationService providerReservationService;

    @GetMapping
    public ResponseEntity<Page<ProviderReservationResponse>> getReservations(
            @PathVariable Long providerId,
            @RequestParam(required = false) ReservationStatus status,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {

        return ResponseEntity.ok(
                providerReservationService.getReservationsForProvider(providerId, status, pageable));
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ProviderReservationResponse> getReservationById(
            @PathVariable Long providerId,
            @PathVariable Long reservationId) {

        return ResponseEntity.ok(
                providerReservationService.getReservationById(providerId, reservationId));
    }

    @PatchMapping("/{reservationId}/accept")
    public ResponseEntity<ProviderReservationResponse> acceptReservation(
            @PathVariable Long providerId,
            @PathVariable Long reservationId,
            @RequestBody(required = false) ProviderReservationActionRequest request) {

        return ResponseEntity.ok(
                providerReservationService.acceptReservation(providerId, reservationId, request));
    }

    @PatchMapping("/{reservationId}/reject")
    public ResponseEntity<ProviderReservationResponse> rejectReservation(
            @PathVariable Long providerId,
            @PathVariable Long reservationId,
            @Valid @RequestBody ProviderReservationActionRequest request) {

        return ResponseEntity.ok(
                providerReservationService.rejectReservation(providerId, reservationId, request));
    }

    @PatchMapping("/{reservationId}/complete")
    public ResponseEntity<ProviderReservationResponse> completeReservation(
            @PathVariable Long providerId,
            @PathVariable Long reservationId) {

        return ResponseEntity.ok(
                providerReservationService.completeReservation(providerId, reservationId));
    }

    @PatchMapping("/{reservationId}/cancel")
    public ResponseEntity<ProviderReservationResponse> cancelReservation(
            @PathVariable Long providerId,
            @PathVariable Long reservationId,
            @Valid @RequestBody ProviderReservationActionRequest request) {

        return ResponseEntity.ok(
                providerReservationService.cancelReservation(providerId, reservationId, request));
    }

    @GetMapping("/history")
    public ResponseEntity<Page<ProviderReservationResponse>> getBookingHistory(
            @PathVariable Long providerId,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {

        return ResponseEntity.ok(
                providerReservationService.getBookingHistory(providerId, pageable));
    }

    @GetMapping("/statistics")
    public ResponseEntity<ProviderReservationStatistics> getStatistics(
            @PathVariable Long providerId) {

        return ResponseEntity.ok(
                providerReservationService.getProviderReservationStatistics(providerId));
    }

    @GetMapping("/pending-count")
    public ResponseEntity<Long> getPendingCount(@PathVariable Long providerId) {
        return ResponseEntity.ok(
                providerReservationService.getPendingReservationCount(providerId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProviderReservationResponse>> searchReservations(
            @PathVariable Long providerId,
            @RequestParam String keyword,
            @RequestParam(required = false) ReservationStatus status,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {

        return ResponseEntity.ok(
                providerReservationService.searchReservations(providerId, keyword, status, pageable));
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportReservations(
            @PathVariable Long providerId,
            @RequestParam(required = false) ReservationStatus status) {

        String csv = providerReservationService.exportReservationsAsCsv(providerId, status);
        String filename = "reservations-provider-" + providerId
                + (status != null ? "-" + status.name().toLowerCase() : "")
                + ".csv";

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .header("Content-Type", "text/csv")
                .body(csv);
    }
}