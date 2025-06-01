package com.moviebooking.service;

import com.moviebooking.dto.FoodItemDTO;
import com.moviebooking.entity.FoodItem;
import com.moviebooking.repository.FoodItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodItemService {
    private final FoodItemRepository foodItemRepository;

    public List<FoodItemDTO> getFoodItemsByCategory(String category) {
        if ("ALL".equalsIgnoreCase(category)) {
            return foodItemRepository.findByIsActiveTrue()
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        }
        return foodItemRepository.findByCategoryAndIsActiveTrue(FoodItem.Category.valueOf(category))
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public FoodItemDTO createFoodItem(FoodItemDTO foodItemDTO) {
        FoodItem foodItem = new FoodItem();
        mapToEntity(foodItemDTO, foodItem);
        foodItem = foodItemRepository.save(foodItem);
        return mapToDTO(foodItem);
    }

    private FoodItemDTO mapToDTO(FoodItem foodItem) {
        FoodItemDTO dto = new FoodItemDTO();
        dto.setId(foodItem.getId());
        dto.setName(foodItem.getName());
        dto.setDescription(foodItem.getDescription());
        dto.setPrice(foodItem.getPrice());
        dto.setImageUrl(foodItem.getImageUrl());
        dto.setCategory(foodItem.getCategory().name());
        dto.setActive(foodItem.isActive());
        return dto;
    }

    private void mapToEntity(FoodItemDTO dto, FoodItem foodItem) {
        foodItem.setName(dto.getName());
        foodItem.setDescription(dto.getDescription());
        foodItem.setPrice(dto.getPrice());
        foodItem.setImageUrl(dto.getImageUrl());
        foodItem.setCategory(FoodItem.Category.valueOf(dto.getCategory()));
        foodItem.setActive(dto.getIsActive());
    }

    public FoodItemDTO updateFoodItem(FoodItemDTO foodItemDTO) {
        FoodItem foodItem = foodItemRepository.findById(foodItemDTO.getId())
                .orElseThrow(() -> new RuntimeException("Food item not found with id: " + foodItemDTO.getId()));
        mapToEntity(foodItemDTO, foodItem);
        foodItem = foodItemRepository.save(foodItem);
        return mapToDTO(foodItem);
    }

    public void deleteFoodItem(Integer id) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food item not found with id: " + id));
        foodItemRepository.delete(foodItem);
    }
}
