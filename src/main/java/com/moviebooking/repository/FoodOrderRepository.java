package com.moviebooking.repository;

import com.moviebooking.entity.FoodOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Integer> {
    //@EntityGraph(attributePaths = {"foodItem"})
    List<FoodOrder> findByTicketId(Integer ticketId);
}