package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.ReviewHelpful;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewHelpfulRepository extends JpaRepository<ReviewHelpful, Long> {

    boolean existsByReviewIdAndUserId(Long reviewId, Long userId);

    Optional<ReviewHelpful> findByReviewIdAndUserId(Long reviewId, Long userId);

    long countByReviewId(Long reviewId);
}