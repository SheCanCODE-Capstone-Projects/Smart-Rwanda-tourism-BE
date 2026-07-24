package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    List<LoginHistory> findByUserIdOrderByLoginTimeDesc(Long userId);  // ← Changed from User to Long
    List<LoginHistory> findTop10ByUserIdOrderByLoginTimeDesc(Long userId);  // ← Changed from User to Long
}
