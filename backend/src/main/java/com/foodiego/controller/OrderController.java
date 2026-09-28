package com.foodiego.controller;

import com.foodiego.dto.CheckoutRequest;
import com.foodiego.entity.Order;
import com.foodiego.entity.User;
import com.foodiego.security.AuthUtil;
import com.foodiego.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final AuthUtil authUtil;

    public OrderController(OrderService orderService, AuthUtil authUtil) {
        this.orderService = orderService;
        this.authUtil = authUtil;
    }

    @PostMapping("/checkout")
    public Order checkout(HttpServletRequest request, @RequestBody CheckoutRequest req) {
        User user = requireUser(request);
        return orderService.checkout(user, req);
    }

    @GetMapping
    public List<Order> history(HttpServletRequest request) {
        User user = requireUser(request);
        return orderService.getOrderHistory(user);
    }

    @GetMapping("/{id}")
    public Order track(HttpServletRequest request, @PathVariable Long id) {
        User user = requireUser(request);
        return orderService.getOrder(id, user);
    }

    private User requireUser(HttpServletRequest request) {
        return authUtil.getCurrentUser(request)
                .orElseThrow(() -> new IllegalArgumentException("Please log in to continue."));
    }
}
