package com.ishant.ticket_booking.controller;

import com.ishant.ticket_booking.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        String token = service.register(request.name(), request.email(), request.password());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        String token = service.authenticate(request.email(), request.password());
        return ResponseEntity.ok(new AuthResponse(token));
    }
    //DTOs

    public record RegisterRequest(
            String name,
            String email,
            String password
    ) {}

    public record AuthRequest(
            String email,
            String password
    ) {}

    public record AuthResponse(
            String token
    ) {}
}