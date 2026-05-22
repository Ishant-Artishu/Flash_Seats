package com.ishant.ticket_booking.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest(properties = {"spring.docker.compose.skip.in-tests=true"})
public class BookingServiceConcurrencyTest {

    @Autowired
    private BookingService bookingService;

    // This forces the test to use H2 memory database instead of Postgres
    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:postgresql://localhost:5432/postgres");
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () ->
                System.getenv().getOrDefault("SPRING_DATASOURCE_PASSWORD", "dummy_test_password"));

        registry.add("spring.data.redis.host", () -> "localhost");
        registry.add("spring.data.redis.port", () -> "6379");

        registry.add("jwt.secret", () -> "THIS_IS_A_DUMMY_KEY_FOR_TESTING_ONLY_32_CHARS");
    }

    @Test
    public void testConcurrentBooking() throws InterruptedException {
        int numberOfThreads = 10;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        // Simulate 10 users trying to book 1 ticket simultaneously
        for (int i = 0; i < numberOfThreads; i++) {
            service.submit(() -> {
                try {
                    // Assuming you have an event with ID 1
                    bookingService.bookTicket(1L, 1L, 1);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        service.shutdown();

        // Assertions would go here to check that only 10 tickets were booked
    }
}