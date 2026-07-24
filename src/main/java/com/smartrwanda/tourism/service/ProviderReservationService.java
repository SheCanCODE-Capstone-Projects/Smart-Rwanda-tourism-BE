package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.dto.ProviderReservationActionRequest;
import com.smartrwanda.tourism.dto.ProviderReservationResponse;
import com.smartrwanda.tourism.dto.ProviderReservationStatistics;
import com.smartrwanda.tourism.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProviderReservationService {

    Page<ProviderReservationResponse> getReservationsForProvider(Long providerId,
                                                                 ReservationStatus status,
                                                                 Pageable pageable);

    ProviderReservationResponse getReservationById(Long providerId, Long reservationId);

    ProviderReservationResponse acceptReservation(Long providerId,
                                                  Long reservationId,
                                                  ProviderReservationActionRequest request);

    ProviderReservationResponse rejectReservation(Long providerId,
                                                  Long reservationId,
                                                  ProviderReservationActionRequest request);

    ProviderReservationResponse completeReservation(Long providerId, Long reservationId);

    ProviderReservationResponse cancelReservation(Long providerId,
                                                  Long reservationId,
                                                  ProviderReservationActionRequest request);

    Page<ProviderReservationResponse> getBookingHistory(Long providerId, Pageable pageable);

    ProviderReservationStatistics getProviderReservationStatistics(Long providerId);


    long getPendingReservationCount(Long providerId);


    Page<ProviderReservationResponse> searchReservations(Long providerId,
                                                         String keyword,
                                                         ReservationStatus status,
                                                         Pageable pageable);


    String exportReservationsAsCsv(Long providerId, ReservationStatus status);
}