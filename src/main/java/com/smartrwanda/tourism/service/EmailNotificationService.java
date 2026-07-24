package com.smartrwanda.tourism.service;

import com.smartrwanda.tourism.entity.Provider;
import com.smartrwanda.tourism.entity.Reservation;
import com.smartrwanda.tourism.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final JavaMailSender mailSender;
    private final String frontendUrl = "http://localhost:3000";

    public void sendBookingRequestEmail(Provider provider, Reservation reservation) {
        try {
            String subject = " New Booking Request - RwandaWays";
            String body = buildBookingRequestEmail(provider, reservation);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(provider.getContactEmail());
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            System.out.println("Booking request email sent to: " + provider.getContactEmail());
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public void sendBookingConfirmationEmail(User user, Reservation reservation, Provider provider) {
        try {
            String subject = " Booking Confirmed - Smart Rwanda Tourism";
            String body = buildBookingConfirmationEmail(user, reservation, provider);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            System.out.println(" Booking confirmation email sent to: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public void sendNewMessageEmail(User recipient, Provider sender, String messageContent) {
        try {
            String subject = " New Message from " + sender.getBusinessName() + " - RwandaWays";
            String body = String.format(
                    "Hello %s,\n\n" +
                            "You have received a new message from %s regarding your booking:\n\n" +
                            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                            "%s\n" +
                            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                            "Log in to your dashboard to reply:\n" +
                            "%s/dashboard\n\n" +
                            "Best regards,\n" +
                            "RwandaWays Team 🌍",
                    recipient.getFirstName(),
                    sender.getBusinessName(),
                    messageContent,
                    frontendUrl
            );

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipient.getEmail());
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            System.out.println("New message email sent to: " + recipient.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    private String buildBookingRequestEmail(Provider provider, Reservation reservation) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String checkIn = reservation.getCheckInDate().format(dateFormatter);
        String checkOut = reservation.getCheckOutDate().format(dateFormatter);

        return String.format(
                "Hello %s,\n\n" +
                        "You have received a new booking request through RwandaWays!\n\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "GUEST DETAILS\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "Name: %s %s\n" +
                        "Email: %s\n" +
                        "Phone: %s\n\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        " BOOKING DETAILS\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "Booking ID: %s\n" +
                        "Check-in: %s\n" +
                        "Check-out: %s\n" +
                        "Guests: %d\n" +
                        "Room Type: %s\n" +
                        "Special Requests: %s\n\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        " PAYMENT INSTRUCTIONS\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "RwandaWays does not handle payments.\n" +
                        "Please contact the guest directly to arrange payment.\n\n" +
                        "You can send a payment link via MTN MoMo, Flutterwave,\n" +
                        "or provide your bank details for transfer.\n\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        " WHAT TO DO NEXT\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "1. Contact the guest within 24 hours\n" +
                        "2. Arrange payment directly\n" +
                        "3. Log in to confirm the booking:\n" +
                        "   %s/provider/dashboard\n\n" +
                        "Best regards,\n" +
                        "RwandaWays Team 🌍",
                provider.getBusinessName(),
                reservation.getUser().getFirstName(),
                reservation.getUser().getLastName(),
                reservation.getUser().getEmail(),
                reservation.getUser().getPhoneNumber() != null ? reservation.getUser().getPhoneNumber() : "Not provided",
                reservation.getConfirmationCode(),
                checkIn,
                checkOut,
                reservation.getNumberOfGuests() != null ? reservation.getNumberOfGuests() : 1,
                reservation.getRoomType() != null ? reservation.getRoomType() : "Standard",
                reservation.getSpecialRequests() != null ? reservation.getSpecialRequests() : "None",
                frontendUrl
        );
    }

    private String buildBookingConfirmationEmail(User user, Reservation reservation, Provider provider) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String checkIn = reservation.getCheckInDate().format(dateFormatter);
        String checkOut = reservation.getCheckOutDate().format(dateFormatter);

        return String.format(
                "Hello %s,\n\n" +
                        "Your booking has been CONFIRMED! \n\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        " BOOKING DETAILS\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "Booking ID: %s\n" +
                        "Provider: %s\n" +
                        "Check-in: %s\n" +
                        "Check-out: %s\n" +
                        "Guests: %d\n" +
                        "Room Type: %s\n\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        " PROVIDER CONTACT\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "Business: %s\n" +
                        "Email: %s\n" +
                        "Phone: %s\n" +
                        "Location: %s\n\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        " PAYMENT INFORMATION\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "Please contact the provider directly to arrange payment.\n\n" +
                        "Best regards,\n" +
                        "RwandaWays Team 🌍",
                user.getFirstName(),
                reservation.getConfirmationCode(),
                provider.getBusinessName(),
                checkIn,
                checkOut,
                reservation.getNumberOfGuests() != null ? reservation.getNumberOfGuests() : 1,
                reservation.getRoomType() != null ? reservation.getRoomType() : "Standard",
                provider.getBusinessName(),
                provider.getContactEmail(),
                provider.getContactPhone() != null ? provider.getContactPhone() : "Not provided",
                provider.getLocation()
        );
    }
}