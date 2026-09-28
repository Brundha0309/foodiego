package com.foodiego.controller;

import com.foodiego.entity.MenuItem;
import com.foodiego.repository.MenuItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    private final MenuItemRepository menuItemRepository;

    public MenuItemController(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @GetMapping
    public List<MenuItem> getAll(@RequestParam(required = false) String category,
                                  @RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return menuItemRepository.findByNameContainingIgnoreCase(search);
        }
        if (category != null && !category.isBlank() && !category.equalsIgnoreCase("all")) {
            return menuItemRepository.findByCategory_NameIgnoreCase(category);
        }
        return menuItemRepository.findAll();
    }

    @GetMapping("/{id}")
    public MenuItem getOne(@PathVariable Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
    }
}
