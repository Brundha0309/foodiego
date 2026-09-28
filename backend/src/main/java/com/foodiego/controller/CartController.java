package com.foodiego.controller;

import com.foodiego.dto.CartItemRequest;
import com.foodiego.entity.CartItem;
import com.foodiego.entity.User;
import com.foodiego.security.AuthUtil;
import com.foodiego.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final AuthUtil authUtil;

    public CartController(CartService cartService, AuthUtil authUtil) {
        this.cartService = cartService;
        this.authUtil = authUtil;
    }

    @GetMapping
    public List<CartItem> getCart(HttpServletRequest request) {
        User user = requireUser(request);
        return cartService.getCart(user);
    }

    @PostMapping("/add")
    public CartItem add(HttpServletRequest request, @RequestBody CartItemRequest req) {
        User user = requireUser(request);
        return cartService.addToCart(user, req.getMenuItemId(), req.getQuantity() == null ? 1 : req.getQuantity());
    }

    @PutMapping("/{cartItemId}")
    public CartItem update(HttpServletRequest request, @PathVariable Long cartItemId, @RequestBody CartItemRequest req) {
        User user = requireUser(request);
        return cartService.updateQuantity(user, cartItemId, req.getQuantity());
    }

    @DeleteMapping("/{cartItemId}")
    public void remove(HttpServletRequest request, @PathVariable Long cartItemId) {
        User user = requireUser(request);
        cartService.removeItem(user, cartItemId);
    }

    private User requireUser(HttpServletRequest request) {
        return authUtil.getCurrentUser(request)
                .orElseThrow(() -> new IllegalArgumentException("Please log in to continue."));
    }
}
