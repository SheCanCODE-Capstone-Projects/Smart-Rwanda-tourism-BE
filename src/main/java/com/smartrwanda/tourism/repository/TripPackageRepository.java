package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.TripPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TripPackageRepository extends JpaRepository<TripPackage, Long> {

    List<TripPackage> findByIsActiveTrue();

    List<TripPackage> findByIsFeaturedTrue();

    @Query("SELECT p FROM TripPackage p WHERE p.isActive = true AND " +
            "p.numberOfDays BETWEEN :minDays AND :maxDays AND " +
            "p.minBudget <= :budget AND p.maxBudget >= :budget")
    List<TripPackage> findMatchingPackages(
            @Param("minDays") Integer minDays,
            @Param("maxDays") Integer maxDays,
            @Param("budget") BigDecimal budget);

    @Query("SELECT p FROM TripPackage p WHERE p.isActive = true AND " +
            "EXISTS (SELECT i FROM p.interests i WHERE LOWER(i) LIKE LOWER(CONCAT('%', :interest, '%')))")
    List<TripPackage> findByInterest(@Param("interest") String interest);
}