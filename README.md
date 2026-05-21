# Ticket Booking Engine

A high-concurrency ticket booking backend built with **Spring Boot, PostgreSQL, and Redis**. This project is engineered to handle simultaneous seat reservations at scale, ensuring data integrity during peak traffic.

## Project Description

This engine addresses the core challenge of **preventing overselling and race conditions** in high-traffic ticketing systems. It uses distributed locking via **Redisson and Redis** to ensure atomic operations, guaranteeing that no two users can book the same seat simultaneously.

## Technical Highlights

* **Concurrency Control:** Implements **Redis-based distributed locks** for atomic seat reservations and transaction safety.
* **Secure API Architecture:** Features a robust **JWT authentication** filter chain with Spring Security for stateless access control.
* **Infrastructure:** Fully containerized with **Docker and Docker Compose** for seamless local and production deployments.
* **Scalable Configuration:** Uses environment variable injection for secure, production-ready secret management without hardcoding keys.
* **Database Reliability:** Built on **PostgreSQL with Spring Data JPA** to ensure ACID-compliant transaction logs.

## Getting Started

### Prerequisites

* [Docker](https://www.docker.com/) & Docker Compose
* [JDK 25](https://openjdk.java.net/projects/jdk/25/)
* [Maven](https://maven.apache.org/)

### Setup

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/yourusername/ticket-booking.git](https://github.com/yourusername/ticket-booking.git)
   cd ticket-booking
