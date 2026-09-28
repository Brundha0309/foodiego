package com.foodiego.controller;

import com.foodiego.entity.MenuItem;
import com.foodiego.entity.Restaurant;
import com.foodiego.repository.MenuItemRepository;
import com.foodiego.repository.RestaurantRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantController(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @GetMapping
    public List<Restaurant> getAll(@RequestParam(required = false) String category,
                                    @RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return restaurantRepository.findByNameContainingIgnoreCase(search);
        }
        if (category != null && !category.isBlank() && !category.equalsIgnoreCase("all")) {
            return restaurantRepository.findByCategory_NameIgnoreCase(category);
        }
        return restaurantRepository.findAll();
    }

    @GetMapping("/{id}")
    public Restaurant getOne(@PathVariable Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
    }

    @GetMapping("/{id}/menu")
    public List<MenuItem> getMenu(@PathVariable Long id) {
        return menuItemRepository.findByRestaurant_Id(id);
    }
}
