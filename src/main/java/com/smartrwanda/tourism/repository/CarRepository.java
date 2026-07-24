package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByProviderId(Long providerId);
}
