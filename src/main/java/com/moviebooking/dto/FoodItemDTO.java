package com.moviebooking.dto;

import lombok.Data;

@Data
public class FoodItemDTO {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private String category;
    private boolean isActive;
}
