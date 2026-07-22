package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.Reservation;
import com.smartrwanda.tourism.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // ===== BASIC FIND METHODS =====

    // Find by user ID
    List<Reservation> findByUserId(Long userId);

    // Find by provider ID
    List<Reservation> findByProviderId(Long providerId);

    // Find by status
    List<Reservation> findByStatus(String status);

    // Find by user ID and status
    List<Reservation> findByUserIdAndStatus(Long userId, String status);

    // Find by provider ID and status
    List<Reservation> findByProviderIdAndStatus(Long providerId, ReservationStatus status);

    // Find by user ID and status (for UserReservationServiceImpl)
    List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status);

    // Find by ID and provider ID
    List<Reservation> findByIdAndProviderId(Long id, Long providerId);

    // Find by user ID ordered by creation date
    List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId);

    // ===== COUNT METHODS =====

    // Count by provider
    long countByProviderId(Long providerId);

    // Count by status
    long countByStatus(String status);

    // Count by provider ID and status
    long countByProviderIdAndStatus(Long providerId, ReservationStatus status);

    // Count by user ID and status
    long countByUserIdAndStatus(Long userId, ReservationStatus status);

    // ===== SEARCH METHOD =====

    // Search by provider ID with filters
    @Query("SELECT r FROM Reservation r WHERE r.providerId = :providerId " +
            "AND (:searchText IS NULL OR :searchText = '' OR r.user.email LIKE %:searchText% OR r.user.firstName LIKE %:searchText%) " +
            "AND (:status IS NULL OR r.status = :status)")
    Page<Reservation> searchByProviderId(@Param("providerId") Long providerId,
                                         @Param("searchText") String searchText,
                                         @Param("status") ReservationStatus status,
                                         Pageable pageable);

    // ===== SUM/AGGREGATE METHODS =====

    // Sum total price by provider ID and status
    @Query("SELECT COALESCE(SUM(r.totalPrice), 0) FROM Reservation r WHERE r.providerId = :providerId AND r.status = :status")
    Double sumTotalPriceByProviderIdAndStatus(@Param("providerId") Long providerId,
                                              @Param("status") ReservationStatus status);
}