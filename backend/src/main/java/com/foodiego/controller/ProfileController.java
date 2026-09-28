package com.foodiego.controller;

import com.foodiego.entity.User;
import com.foodiego.repository.UserRepository;
import com.foodiego.security.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final AuthUtil authUtil;
    private final UserRepository userRepository;

    public ProfileController(AuthUtil authUtil, UserRepository userRepository) {
        this.authUtil = authUtil;
        this.userRepository = userRepository;
    }

    @GetMapping
    public User getProfile(HttpServletRequest request) {
        return requireUser(request);
    }

    @PutMapping
    public User updateProfile(HttpServletRequest request, @RequestBody User updates) {
        User user = requireUser(request);
        if (updates.getFullName() != null) user.setFullName(updates.getFullName());
        if (updates.getPhone() != null) user.setPhone(updates.getPhone());
        return userRepository.save(user);
    }

    private User requireUser(HttpServletRequest request) {
        return authUtil.getCurrentUser(request)
                .orElseThrow(() -> new IllegalArgumentException("Please log in to continue."));
    }
}
