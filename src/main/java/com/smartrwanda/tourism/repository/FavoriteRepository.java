package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.Favorite;
import com.smartrwanda.tourism.enums.ReviewTargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserId(Long userId);

    List<Favorite> findByUserIdAndTargetType(Long userId, ReviewTargetType targetType);

    Optional<Favorite> findByUserIdAndTargetTypeAndTargetId(Long userId, ReviewTargetType targetType, Long targetId);

    boolean existsByUserIdAndTargetTypeAndTargetId(Long userId, ReviewTargetType targetType, Long targetId);

    long countByUserId(Long userId);

    void deleteByUserIdAndTargetTypeAndTargetId(Long userId, ReviewTargetType targetType, Long targetId);
}
