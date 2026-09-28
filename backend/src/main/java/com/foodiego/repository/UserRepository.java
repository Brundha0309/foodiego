package com.foodiego.repository;

import com.foodiego.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByAuthToken(String authToken);
    boolean existsByEmail(String email);
}
