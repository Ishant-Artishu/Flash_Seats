package com.ishant.ticket_booking.controller;

import com.ishant.ticket_booking.entity.User;
import com.ishant.ticket_booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<String> bookTicket(
            @RequestBody BookingRequest request,
            @AuthenticationPrincipal User currentUser
    ) {

        Long userId = currentUser.getId();

        bookingService.bookTicket(userId, request.eventId(), request.quantity());

        return ResponseEntity.ok(request.quantity() + " ticket(s) booked successfully for " + currentUser.getEmail());
    }

    // Updated DTO record to include the required ticket quantity
    public record BookingRequest(Long eventId, Integer quantity) {}
}