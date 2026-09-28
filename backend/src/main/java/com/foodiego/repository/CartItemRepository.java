package com.foodiego.repository;

import com.foodiego.entity.CartItem;
import com.foodiego.entity.MenuItem;
import com.foodiego.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndMenuItem(User user, MenuItem menuItem);
    void deleteByUser(User user);
}
