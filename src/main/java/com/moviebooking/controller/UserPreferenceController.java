package com.moviebooking.controller;

import com.moviebooking.dto.UserPreferenceDTO;
import com.moviebooking.service.UserPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-preferences")
@RequiredArgsConstructor
public class UserPreferenceController {
    private final UserPreferenceService userPreferenceService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UserPreferenceDTO>> getPreferencesByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(userPreferenceService.getPreferencesByUserId(userId));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserPreferenceDTO> createPreference(@RequestBody UserPreferenceDTO preferenceDTO) {
        return ResponseEntity.ok(userPreferenceService.createPreference(preferenceDTO));
    }
}
