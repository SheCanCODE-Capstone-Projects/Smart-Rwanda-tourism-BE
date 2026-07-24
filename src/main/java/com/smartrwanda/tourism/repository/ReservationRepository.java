package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.Reservation;
import com.smartrwanda.tourism.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {



    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status);

    List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserIdAndStatus(Long userId, ReservationStatus status);

    long countByUserId(Long userId);


    List<Reservation> findByProviderId(Long providerId);

    List<Reservation> findByProviderIdAndStatus(Long providerId, ReservationStatus status);

    Page<Reservation> findByProviderId(Long providerId, Pageable pageable);

    Page<Reservation> findByProviderIdAndStatus(Long providerId, ReservationStatus status, Pageable pageable);

    long countByProviderId(Long providerId);

    long countByProviderIdAndStatus(Long providerId, ReservationStatus status);

    Optional<Reservation> findByIdAndProviderId(Long id, Long providerId);


    List<Reservation> findByStatus(String status);

    long countByStatus(ReservationStatus status);



    @Query("SELECT r FROM Reservation r WHERE r.provider.id = :providerId " +
            "AND (:searchText IS NULL OR :searchText = '' OR LOWER(r.user.email) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(r.user.firstName) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND (:status IS NULL OR r.status = :status)")
    Page<Reservation> searchByProviderId(@Param("providerId") Long providerId,
                                         @Param("searchText") String searchText,
                                         @Param("status") ReservationStatus status,
                                         Pageable pageable);



    @Query("SELECT COUNT(r) FROM Reservation r WHERE DATE(r.createdAt) = :date")
    long countByCreatedAtDate(@Param("date") LocalDate date);


    @Query("SELECT COUNT(r) FROM Reservation r WHERE EXTRACT(MONTH FROM r.createdAt) = :month AND EXTRACT(YEAR FROM r.createdAt) = :year")
    long countByCreatedAtMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COALESCE(SUM(r.totalPrice), 0) FROM Reservation r WHERE r.provider.id = :providerId AND r.status = :status")
    Double sumTotalPriceByProviderIdAndStatus(@Param("providerId") Long providerId,
                                              @Param("status") ReservationStatus status);
}