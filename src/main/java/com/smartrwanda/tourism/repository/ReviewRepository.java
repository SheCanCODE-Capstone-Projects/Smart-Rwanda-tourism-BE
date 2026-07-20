package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.Review;
import com.smartrwanda.tourism.enums.ReviewStatus;
import com.smartrwanda.tourism.enums.ReviewTargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByTargetTypeAndTargetIdAndStatus(
            ReviewTargetType targetType, Long targetId, ReviewStatus status);

    List<Review> findByTargetTypeAndTargetIdAndRatingAndStatus(
            ReviewTargetType targetType, Long targetId, Integer rating, ReviewStatus status);

    List<Review> findByUserId(Long userId);

    List<Review> findByStatus(ReviewStatus status);

    boolean existsByUserIdAndTargetTypeAndTargetId(
            Long userId, ReviewTargetType targetType, Long targetId);

    @Query("SELECT AVG(r.rating) FROM Review r " +
            "WHERE r.targetType = :targetType AND r.targetId = :targetId AND r.status = 'ACTIVE'")
    Double findAverageRating(@Param("targetType") ReviewTargetType targetType,
                             @Param("targetId") Long targetId);

    long countByTargetTypeAndTargetIdAndStatus(
            ReviewTargetType targetType, Long targetId, ReviewStatus status);

    long countByTargetTypeAndTargetIdAndRatingAndStatus(
            ReviewTargetType targetType, Long targetId, Integer rating, ReviewStatus status);
}