package com.moviebooking.controller;

import com.moviebooking.dto.LoyaltyPointDTO;
import com.moviebooking.dto.UserDTO;
import com.moviebooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateProfile(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateProfile(id, userDTO));
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