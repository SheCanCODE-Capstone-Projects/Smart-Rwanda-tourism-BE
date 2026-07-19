package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.Reservation;
import com.smartrwanda.tourism.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {


    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status);

    List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserIdAndStatus(Long userId, ReservationStatus status);



    List<Reservation> findByProviderId(Long providerId);

    List<Reservation> findByProviderIdAndStatus(Long providerId, ReservationStatus status);

    List<Reservation> findByProviderIdOrderByCreatedAtDesc(Long providerId);

    long countByProviderIdAndStatus(Long providerId, ReservationStatus status);



    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.provider.id = :providerId AND r.status = 'PENDING'")
    long countPendingReservations(@Param("providerId") Long providerId);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.provider.id = :providerId AND r.status = 'COMPLETED'")
    long countCompletedReservations(@Param("providerId") Long providerId);

    @Query("SELECT SUM(r.totalPrice) FROM Reservation r WHERE r.provider.id = :providerId AND r.status = 'COMPLETED'")
    Double sumCompletedRevenue(@Param("providerId") Long providerId);
}