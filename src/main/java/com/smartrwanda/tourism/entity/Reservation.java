package com.smartrwanda.tourism.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @Column(name = "reservation_date", nullable = false)
    private LocalDateTime reservationDate;

    @Column(name = "special_requests", length = 500)
    private String specialRequests;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.PENDING;

    @Column(name = "confirmation_code", unique = true)
    private String confirmationCode;


    @Column(name = "guest_name", nullable = false)
    private String guestName;

    @Column(name = "guest_email", nullable = false)
    private String guestEmail;

    @Column(name = "guest_phone")
    private String guestPhone;



    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "currency")
    private String currency = "RWF";


    @Column(name = "check_in_date")
    private LocalDateTime checkInDate;

    @Column(name = "check_out_date")
    private LocalDateTime checkOutDate;

    @Column(name = "number_of_guests")
    private Integer numberOfGuests;

    @Column(name = "room_type")
    private String roomType;


    @Column(name = "pickup_date")
    private LocalDateTime pickupDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "vehicle_type")
    private String vehicleType;


    @Column(name = "tour_date")
    private LocalDateTime tourDate;

    @Column(name = "tour_package")
    private String tourPackage;

    @Column(name = "number_of_people")
    private Integer numberOfPeople;


    @Column(name = "visit_date")
    private LocalDateTime visitDate;

    @Column(name = "number_of_visitors")
    private Integer numberOfVisitors;


    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "rejection_reason")
    private String rejectionReason;
}