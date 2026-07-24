package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.DishRequest;
import com.smartrwanda.tourism.dto.DishResponse;
import java.util.List;

public interface DishService {
    DishResponse create(Long providerId, DishRequest request);
    DishResponse getById(Long id);
    List<DishResponse> getByProvider(Long providerId);
    DishResponse update(Long id, DishRequest request);
    void delete(Long id);
}
