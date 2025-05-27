package com.moviebooking.repository;

import com.moviebooking.entity.LoyaltyPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoyaltyPointRepository extends JpaRepository<LoyaltyPoint, Integer> {
    List<LoyaltyPoint> findByUserId(Integer userId);
}