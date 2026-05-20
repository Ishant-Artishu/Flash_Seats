package com.ishant.ticket_booking.service;

import com.ishant.ticket_booking.entity.Booking;
import com.ishant.ticket_booking.entity.BookingStatus;
import com.ishant.ticket_booking.entity.Event;
import com.ishant.ticket_booking.entity.User;
import com.ishant.ticket_booking.repository.BookingRepository;
import com.ishant.ticket_booking.repository.EventRepository;
import com.ishant.ticket_booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate; // IMPORT THIS

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final RedissonClient redissonClient;

    // Inject TransactionTemplate to control DB commits manually
    private final TransactionTemplate transactionTemplate;

    // Notice we removed @Transactional from here!
    public Booking bookTicket(Long userId, Long eventId, Integer requestedSeats) {
        String lockKey = "event_lock_" + eventId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(3, 10, TimeUnit.SECONDS);

            if (!isLocked) {
                throw new IllegalStateException("Traffic is too high right now! Please try again.");
            }

            // We wrap the DB logic inside the TransactionTemplate.
            // Spring guarantees this block will fully commit to Postgres BEFORE it moves on.
            return transactionTemplate.execute(status -> {

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

                Event event = eventRepository.findByIdWithPessimisticLock(eventId)
                        .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));

                if (event.getAvailableSeats() < requestedSeats) {
                    throw new IllegalStateException("Sorry, not enough seats available! Remaining: "
                            + event.getAvailableSeats());
                }

                event.setAvailableSeats(event.getAvailableSeats() - requestedSeats);
                eventRepository.save(event);

                Booking booking = Booking.builder()
                        .user(user)
                        .event(event)
                        .seatsBooked(requestedSeats)
                        .status(BookingStatus.CONFIRMED)
                        .build();

                return bookingRepository.save(booking);
            }); // TRANSACTION COMMITS HERE

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Booking process interrupted.");
        } finally {
            // Lock is released safely AFTER the DB commit is finished
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}