package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByProviderId(Long providerId);
    List<Room> findByProviderIdAndAvailableTrue(Long providerId);
}
