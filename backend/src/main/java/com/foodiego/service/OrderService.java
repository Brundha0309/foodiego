package com.foodiego.service;

import com.foodiego.dto.CheckoutRequest;
import com.foodiego.entity.*;
import com.foodiego.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final CouponRepository couponRepository;

    public OrderService(CartItemRepository cartItemRepository, OrderRepository orderRepository,
                         AddressRepository addressRepository, CouponRepository couponRepository) {
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.couponRepository = couponRepository;
    }

    @Transactional
    public Order checkout(User user, CheckoutRequest req) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Your cart is empty.");
        }

        double itemTotal = cartItems.stream()
                .mapToDouble(ci -> ci.getMenuItem().getPrice() * ci.getQuantity())
                .sum();
        double deliveryFee = 30.0;
        double platformFee = 10.0;
        double discount = 0.0;

        if (req.getCouponCode() != null && !req.getCouponCode().isBlank()) {
            Coupon coupon = couponRepository.findByCodeIgnoreCase(req.getCouponCode()).orElse(null);
            if (coupon != null && itemTotal >= coupon.getMinOrderValue()) {
                switch (coupon.getDiscountType()) {
                    case "PERCENT" -> discount = itemTotal * (coupon.getDiscountValue() / 100.0);
                    case "FLAT" -> discount = coupon.getDiscountValue();
                    case "FREE_DELIVERY" -> deliveryFee = 0.0;
                    default -> discount = 0.0;
                }
            }
        }

        double total = Math.max(itemTotal + deliveryFee + platformFee - discount, 0);

        Order order = new Order();
        order.setUser(user);
        if (req.getAddressId() != null) {
            Address address = addressRepository.findById(req.getAddressId()).orElse(null);
            order.setAddress(address);
        }
        order.setPaymentMethod(req.getPaymentMethod());
        order.setCouponCode(req.getCouponCode());
        order.setItemTotal(round(itemTotal));
        order.setDeliveryFee(round(deliveryFee));
        order.setPlatformFee(round(platformFee));
        order.setDiscount(round(discount));
        order.setTotalPayable(round(total));
        order.setStatus("PLACED");
        order.setPlacedAt(LocalDateTime.now());
        order.setPreparingAt(LocalDateTime.now().plusMinutes(10));
        order.setOutForDeliveryAt(LocalDateTime.now().plusMinutes(25));

        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setItemName(ci.getMenuItem().getName());
            oi.setPrice(ci.getMenuItem().getPrice());
            oi.setQuantity(ci.getQuantity());
            order.getItems().add(oi);
        }

        Order saved = orderRepository.save(order);
        cartItemRepository.deleteByUser(user);
        return saved;
    }

    public List<Order> getOrderHistory(User user) {
        return orderRepository.findByUserOrderByPlacedAtDesc(user);
    }

    public Order getOrder(Long id, User user) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Not authorized");
        }
        LocalDateTime now = LocalDateTime.now();
        if (order.getStatus().equals("PLACED") && now.isAfter(order.getPreparingAt())) {
            order.setStatus("PREPARING");
        }
        if ((order.getStatus().equals("PLACED") || order.getStatus().equals("PREPARING"))
                && now.isAfter(order.getOutForDeliveryAt())) {
            order.setStatus("OUT_FOR_DELIVERY");
        }
        if (!order.getStatus().equals("DELIVERED") && order.getDeliveredAt() == null
                && now.isAfter(order.getOutForDeliveryAt().plusMinutes(20))) {
            order.setStatus("DELIVERED");
            order.setDeliveredAt(now);
        }
        return orderRepository.save(order);
    }

    private double round(double val) {
        return Math.round(val * 100.0) / 100.0;
    }
}