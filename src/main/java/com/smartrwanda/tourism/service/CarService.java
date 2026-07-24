package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.CarRequest;
import com.smartrwanda.tourism.dto.CarResponse;
import java.util.List;

public interface CarService {
    CarResponse create(Long providerId, CarRequest request);
    CarResponse getById(Long id);
    List<CarResponse> getByProvider(Long providerId);
    CarResponse update(Long id, CarRequest request);
    void delete(Long id);
}
