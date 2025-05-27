package com.moviebooking.service;

import com.moviebooking.dto.FoodItemDTO;
import com.moviebooking.dto.FoodOrderDTO;
import com.moviebooking.entity.FoodItem;
import com.moviebooking.entity.FoodOrder;
import com.moviebooking.entity.Ticket;
import com.moviebooking.repository.FoodItemRepository;
import com.moviebooking.repository.FoodOrderRepository;
import com.moviebooking.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodOrderService {
    private final FoodOrderRepository foodOrderRepository;
    private final TicketRepository ticketRepository;
    private final FoodItemRepository foodItemRepository;

    public List<FoodOrderDTO> getOrdersByTicketId(Integer ticketId) {
        return foodOrderRepository.findByTicketId(ticketId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public FoodOrderDTO createFoodOrder(FoodOrderDTO foodOrderDTO) {
        Ticket ticket = ticketRepository.findById(foodOrderDTO.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + foodOrderDTO.getTicketId()));
        FoodItem foodItem = foodItemRepository.findById(foodOrderDTO.getFoodItemId())
                .orElseThrow(() -> new RuntimeException("Food item not found with id: " + foodOrderDTO.getFoodItemId()));
        FoodOrder foodOrder = new FoodOrder();
        foodOrder.setTicket(ticket);
        foodOrder.setFoodItem(foodItem);
        foodOrder.setQuantity(foodOrderDTO.getQuantity());
        foodOrder.setPrice(foodOrderDTO.getPrice());
        foodOrder = foodOrderRepository.save(foodOrder);
        return mapToDTO(foodOrder);
    }

    private FoodOrderDTO mapToDTO(FoodOrder foodOrder) {
        FoodOrderDTO dto = new FoodOrderDTO();
        dto.setId(foodOrder.getId());
        dto.setTicketId(foodOrder.getTicket().getId());
        dto.setFoodItem(mapToFoodItemDTO(foodOrder.getFoodItem())); // Thêm foodItem
        dto.setQuantity(foodOrder.getQuantity());
        dto.setPrice(foodOrder.getPrice());
        dto.setOrderTime(foodOrder.getOrderTime());
        return dto;
    }

    private FoodItemDTO mapToFoodItemDTO(FoodItem foodItem) {
        if (foodItem == null) return null;
        FoodItemDTO dto = new FoodItemDTO();
        dto.setId(foodItem.getId());
        dto.setName(foodItem.getName());
        return dto;
    }
}