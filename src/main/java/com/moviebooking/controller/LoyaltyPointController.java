package com.moviebooking.controller;

import com.moviebooking.dto.LoyaltyPointDTO;
import com.moviebooking.service.LoyaltyPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loyalty-points")
@RequiredArgsConstructor
public class LoyaltyPointController {
    private final LoyaltyPointService loyaltyPointService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<LoyaltyPointDTO>> getPointsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(loyaltyPointService.getPointsByUserId(userId));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LoyaltyPointDTO> addPoints(@RequestBody LoyaltyPointDTO pointDTO) {
        return ResponseEntity.ok(loyaltyPointService.addPoints(pointDTO));
    }
}
