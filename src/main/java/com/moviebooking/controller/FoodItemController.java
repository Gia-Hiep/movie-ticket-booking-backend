package com.moviebooking.controller;

import com.moviebooking.dto.FoodItemDTO;
import com.moviebooking.service.FoodItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food-items")
@RequiredArgsConstructor
public class FoodItemController {
    private final FoodItemService foodItemService;

    @GetMapping("/category/{category}")
    public ResponseEntity<List<FoodItemDTO>> getFoodItemsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(foodItemService.getFoodItemsByCategory(category));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FoodItemDTO> createFoodItem(@RequestBody FoodItemDTO foodItemDTO) {
        return ResponseEntity.ok(foodItemService.createFoodItem(foodItemDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FoodItemDTO> updateFoodItem(@PathVariable Integer id, @RequestBody FoodItemDTO foodItemDTO) {
        foodItemDTO.setId(id);
        return ResponseEntity.ok(foodItemService.updateFoodItem(foodItemDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable Integer id) {
        foodItemService.deleteFoodItem(id);
        return ResponseEntity.noContent().build();
    }
}
