package com.ishant.ticket_booking.config;

import com.ishant.ticket_booking.entity.Event;
import com.ishant.ticket_booking.entity.Role;
import com.ishant.ticket_booking.entity.User;
import com.ishant.ticket_booking.repository.EventRepository;
import com.ishant.ticket_booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Checking database state for seeding...");

        if (userRepository.count() == 0) {
            log.info("Generating 100 mock users with encoded passwords...");

            // Encode the password ONCE outside the loop for performance
            String defaultPassword = passwordEncoder.encode("password123");

            List<User> users = IntStream.rangeClosed(1, 100)
                    .mapToObj(i -> User.builder()
                            .name("Flash Sale User " + i)
                            .email("user" + i + "@example.com")
                            .password(defaultPassword) // Add the encoded password
                            .role(Role.USER)           // Add the RBAC role
                            .build())
                    .collect(Collectors.toList());

            userRepository.saveAll(users);
            log.info("Successfully seeded 100 mock users.");
        }

        if (eventRepository.count() == 0) {
            log.info("Seeding a high-demand concert event...");

            Event flashSaleEvent = Event.builder()
                    .title("Bruno Mars Live - Flash Sale Concert")
                    .totalSeats(100)
                    .availableSeats(100)
                    .build();

            eventRepository.save(flashSaleEvent);
            log.info("Successfully seeded event: '{}' with 100 available seats.", flashSaleEvent.getTitle());
        }

        log.info("Database initialization phase complete.");
    }
}