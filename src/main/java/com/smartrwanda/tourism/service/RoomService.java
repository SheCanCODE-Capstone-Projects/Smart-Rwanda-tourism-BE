package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.RoomRequest;
import com.smartrwanda.tourism.dto.RoomResponse;
import java.util.List;

public interface RoomService {
    RoomResponse create(Long providerId, RoomRequest request);
    RoomResponse getById(Long id);
    List<RoomResponse> getByProvider(Long providerId);
    RoomResponse update(Long id, RoomRequest request);
    void delete(Long id);
}
