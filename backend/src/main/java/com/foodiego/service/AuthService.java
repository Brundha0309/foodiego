package com.foodiego.service;

import com.foodiego.dto.AuthResponse;
import com.foodiego.dto.LoginRequest;
import com.foodiego.dto.SignupRequest;
import com.foodiego.entity.User;
import com.foodiego.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse signup(SignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }
        User user = new User();
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setAuthToken(UUID.randomUUID().toString());
        userRepository.save(user);
        return new AuthResponse(user.getAuthToken(), user.getId(), user.getFullName(), user.getEmail());
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmailOrPhone())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email/phone or password."));
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email/phone or password.");
        }
        user.setAuthToken(UUID.randomUUID().toString());
        userRepository.save(user);
        return new AuthResponse(user.getAuthToken(), user.getId(), user.getFullName(), user.getEmail());
    }
}
