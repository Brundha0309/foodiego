package com.foodiego.repository;

import com.foodiego.entity.Order;
import com.foodiego.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByPlacedAtDesc(User user);
}
