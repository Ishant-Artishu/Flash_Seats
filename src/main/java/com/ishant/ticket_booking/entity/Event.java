package com.ishant.ticket_booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq")
    @SequenceGenerator(name = "event_seq", sequenceName = "event_sequence", allocationSize = 50)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer totalSeats;

    @Column(nullable = false)
    private Integer availableSeats;

    @Version
    private Long version; // Optimistic locking fallback
}