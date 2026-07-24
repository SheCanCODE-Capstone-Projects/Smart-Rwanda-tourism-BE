package com.smartrwanda.tourism.controller;

import com.smartrwanda.tourism.common.ApiResponse;
import com.smartrwanda.tourism.dto.RoomRequest;
import com.smartrwanda.tourism.dto.RoomResponse;
import com.smartrwanda.tourism.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<RoomResponse>> create(
            @PathVariable Long providerId, @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Room added", roomService.create(providerId, request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(roomService.getById(id)));
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getByProvider(@PathVariable Long providerId) {
        return ResponseEntity.ok(ApiResponse.success(roomService.getByProvider(providerId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomResponse>> update(
            @PathVariable Long id, @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Room updated", roomService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Room deleted", null));
    }
}
