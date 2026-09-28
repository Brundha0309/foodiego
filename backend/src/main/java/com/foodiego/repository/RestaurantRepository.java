package com.foodiego.repository;

import com.foodiego.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByCategory_NameIgnoreCase(String categoryName);
    List<Restaurant> findByNameContainingIgnoreCase(String query);
}
