package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FoodOrderDTO {
    private Integer id;
    private Integer ticketId;
    private FoodItemDTO foodItem;
    private Integer foodItemId;
    private Integer quantity;
    private Double price;
    private LocalDateTime orderTime;
}
