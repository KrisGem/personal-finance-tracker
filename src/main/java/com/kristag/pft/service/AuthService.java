package com.kristag.pft.service;

import com.kristag.pft.controller.AuthResponse;
import com.kristag.pft.controller.LoginRequest;
import com.kristag.pft.controller.RegisterRequest;
import com.kristag.pft.domain.entity.User;
import com.kristag.pft.domain.repository.UserRepository;
import com.kristag.pft.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest req) {
        String email = req.email().toLowerCase(Locale.ROOT).trim();

        if (userRepo.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        String hash = passwordEncoder.encode(req.password());
        User user = new User(email, hash);
        userRepo.save(user);

        return new AuthResponse(jwtService.generateToken(user));
    }

    public AuthResponse login(LoginRequest req) {
        String email = req.email().toLowerCase(Locale.ROOT);
 
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return new AuthResponse(jwtService.generateToken(user));
    }
}