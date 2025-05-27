package com.moviebooking.controller;

import com.moviebooking.dto.LoyaltyPointDTO;
import com.moviebooking.dto.UserDTO;
import com.moviebooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.register(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<UserService.AuthResponse> login(@RequestBody UserService.AuthRequest request) {
        UserService.AuthResponse authResponse = userService.login(request);
        return ResponseEntity.ok(authResponse);
    }

    @PutMapping("/{id}/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> updateProfile(@PathVariable Integer id,@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateProfile(id,userDTO));
    }

    @PutMapping("/{id}/membership")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateMembership(@PathVariable Integer id, @RequestBody String membershipLevel) {
        return ResponseEntity.ok(userService.updateMembership(id, membershipLevel));
    }

    @PostMapping("/loyalty-points")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addLoyaltyPoints(@RequestBody LoyaltyPointDTO pointDTO) {
        userService.addLoyaltyPoints(pointDTO);
        return ResponseEntity.ok().build();
    }
}
