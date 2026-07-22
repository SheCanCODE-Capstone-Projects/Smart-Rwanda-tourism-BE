package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByProviderId(Long providerId);

    List<Reservation> findByStatus(String status);
}