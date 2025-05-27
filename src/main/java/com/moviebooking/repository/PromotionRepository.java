package com.moviebooking.repository;

import com.moviebooking.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    Optional<Promotion> findByCodeAndIsActiveTrue(String code);
    List<Promotion> findByStartDateBeforeAndEndDateAfterAndIsActiveTrue(LocalDateTime start, LocalDateTime end);
    List<Promotion> findByIsActiveTrue();
}