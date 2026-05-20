package com.ishant.ticket_booking.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Event ID cannot be null")
    private Long eventId;

    @NotNull(message = "Seats count cannot be null")
    @Min(value = 1, message = "You must book at least 1 seat")
    private Integer seatsBooked;
}