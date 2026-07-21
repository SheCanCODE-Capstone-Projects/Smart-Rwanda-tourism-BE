package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.ProviderRequest;
import com.smartrwanda.tourism.dto.ProviderResponse;
import com.smartrwanda.tourism.dto.ProviderStatistics;
import com.smartrwanda.tourism.dto.ProviderSummaryResponse;
import com.smartrwanda.tourism.entity.ProviderCategory;
import com.smartrwanda.tourism.service.ProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping("/register/{userId}")
    public ResponseEntity<ApiResponse<ProviderResponse>> register(
            @PathVariable Long userId, @Valid @RequestBody ProviderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Provider profile created", providerService.registerProvider(userId, request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProviderResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(providerService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProviderSummaryResponse>>> getAll(
            @RequestParam(required = false) ProviderCategory category) {
        List<ProviderSummaryResponse> providers;
        if (category != null) {
            providers = providerService.getByCategory(category);
        } else {
            providers = providerService.getAll();
        }
        return ResponseEntity.ok(ApiResponse.success(providers));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProviderResponse>> update(
            @PathVariable Long id, @Valid @RequestBody ProviderRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Provider updated", providerService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        providerService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>success("Provider deleted", null));
    }

    // Admin endpoints
    @GetMapping("/admin/pending")
    public ResponseEntity<ApiResponse<List<ProviderSummaryResponse>>> getPending() {
        return ResponseEntity.ok(ApiResponse.success(providerService.getPendingProviders()));
    }

    @PatchMapping("/admin/{id}/verify")
    public ResponseEntity<ApiResponse<ProviderResponse>> verify(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Provider verified", providerService.verifyProvider(id)));
    }

    @PatchMapping("/admin/{id}/reject")
    public ResponseEntity<ApiResponse<ProviderResponse>> reject(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Provider rejected", providerService.rejectProvider(id)));
    }

    @GetMapping("/admin/statistics")
    public ResponseEntity<ApiResponse<ProviderStatistics>> getStatistics() {
        return ResponseEntity.ok(ApiResponse.success(providerService.getStatistics()));
    }
}
