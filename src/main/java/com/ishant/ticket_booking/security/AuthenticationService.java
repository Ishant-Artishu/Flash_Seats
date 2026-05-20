package com.ishant.ticket_booking.security;

import com.ishant.ticket_booking.entity.Role;
import com.ishant.ticket_booking.entity.User;
import com.ishant.ticket_booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(String name, String email, String password) {
        var user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();

        repository.save(user);
        return jwtService.generateToken(user);
    }

    public String authenticate(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        var user = repository.findByEmail(email)
                .orElseThrow();

        return jwtService.generateToken(user);
    }
}