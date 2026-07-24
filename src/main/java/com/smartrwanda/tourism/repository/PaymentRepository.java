package com.smartrwanda.tourism.repository;

import com.smartrwanda.tourism.entity.Payment;
import com.smartrwanda.tourism.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByReservation(Reservation reservation);

    List<Payment> findByReservationId(Long reservationId);

    List<Payment> findByPaymentStatus(String status);

    List<Payment> findByPaymentMethod(String method);
}