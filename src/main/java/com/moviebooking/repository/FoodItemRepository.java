package com.moviebooking.repository;

import com.moviebooking.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Integer> {
    List<FoodItem> findByCategoryAndIsActiveTrue(FoodItem.Category category);
    List<FoodItem> findByIsActiveTrue();
}