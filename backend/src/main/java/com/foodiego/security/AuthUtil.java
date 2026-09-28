package com.foodiego.security;

import com.foodiego.entity.User;
import com.foodiego.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Lightweight token-based auth helper.
 * On login/signup a random UUID token is generated and stored on the User row.
 * The frontend sends it back as "Authorization: Bearer <token>" on every
 * protected request. This keeps the project simple while still being a real,
 * working auth mechanism (passwords are BCrypt-hashed, tokens are opaque).
 */
@Component
public class AuthUtil {

    private final UserRepository userRepository;

    public AuthUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getCurrentUser(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return Optional.empty();
        }
        String token = header.substring(7).trim();
        if (token.isEmpty()) return Optional.empty();
        return userRepository.findByAuthToken(token);
    }
}
