package com.foodiego.repository;

import com.foodiego.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurant_Id(Long restaurantId);
    List<MenuItem> findByCategory_NameIgnoreCase(String categoryName);
    List<MenuItem> findByNameContainingIgnoreCase(String query);
}
