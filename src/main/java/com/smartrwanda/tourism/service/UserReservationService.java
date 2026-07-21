package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.ReservationRequest;
import com.smartrwanda.tourism.dto.ReservationResponse;
import com.smartrwanda.tourism.entity.ReservationStatus;

import java.util.List;

public interface UserReservationService {

    ReservationResponse createReservation(ReservationRequest request, Long userId);

    List<ReservationResponse> getUserReservations(Long userId);

    ReservationResponse getReservationById(Long reservationId, Long userId);

    void cancelReservation(Long reservationId, Long userId, String reason);

    List<ReservationResponse> getUserReservationsByStatus(Long userId, ReservationStatus status);

    long countReservationsByStatus(Long userId, ReservationStatus status);
}