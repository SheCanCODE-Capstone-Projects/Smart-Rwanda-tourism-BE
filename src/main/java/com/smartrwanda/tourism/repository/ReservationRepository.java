package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.Reservation;
import com.smartrwanda.tourism.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {



    Page<Reservation> findByProviderId(Long providerId, Pageable pageable);

    Page<Reservation> findByProviderIdAndStatus(Long providerId, ReservationStatus status, Pageable pageable);


    List<Reservation> findByProviderId(Long providerId);

    List<Reservation> findByProviderIdAndStatus(Long providerId, ReservationStatus status);

    Optional<Reservation> findByIdAndProviderId(Long id, Long providerId);

    long countByProviderIdAndStatus(Long providerId, ReservationStatus status);

    long countByProviderId(Long providerId);

    @Query("SELECT COALESCE(SUM(r.totalPrice), 0) FROM Reservation r " +
            "WHERE r.provider.id = :providerId AND r.status = :status")
    Double sumTotalPriceByProviderIdAndStatus(@Param("providerId") Long providerId,
                                              @Param("status") ReservationStatus status);



    long countByStatus(ReservationStatus status);

    long countByUserId(Long userId);


    @Query(value = "SELECT COUNT(*) FROM reservations WHERE created_at::date = :date",
            nativeQuery = true)
    long countByCreatedAtDate(@Param("date") LocalDate date);

    @Query(value = "SELECT COUNT(*) FROM reservations " +
            "WHERE EXTRACT(MONTH FROM created_at) = :month " +
            "AND EXTRACT(YEAR FROM created_at) = :year",
            nativeQuery = true)
    long countByCreatedAtMonth(@Param("month") int month, @Param("year") int year);



    @Query("SELECT r FROM Reservation r WHERE r.provider.id = :providerId " +
            "AND (:status IS NULL OR r.status = :status) " +
            "AND (LOWER(r.guestName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "     OR LOWER(r.guestEmail) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "     OR LOWER(r.confirmationCode) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Reservation> searchByProviderId(@Param("providerId") Long providerId,
                                         @Param("keyword") String keyword,
                                         @Param("status") ReservationStatus status,
                                         Pageable pageable);



    List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status);

    long countByUserIdAndStatus(Long userId, ReservationStatus status);
}