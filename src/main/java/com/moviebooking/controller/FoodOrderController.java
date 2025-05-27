package com.moviebooking.controller;

import com.moviebooking.dto.FoodOrderDTO;
import com.moviebooking.service.FoodOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food-orders")
@RequiredArgsConstructor
public class FoodOrderController {
    private final FoodOrderService foodOrderService;

    @GetMapping("/ticket/{ticketId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<FoodOrderDTO>> getOrdersByTicketId(@PathVariable Integer ticketId) {
        return ResponseEntity.ok(foodOrderService.getOrdersByTicketId(ticketId));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FoodOrderDTO> createFoodOrder(@RequestBody FoodOrderDTO foodOrderDTO) {
        return ResponseEntity.ok(foodOrderService.createFoodOrder(foodOrderDTO));
    }
}
