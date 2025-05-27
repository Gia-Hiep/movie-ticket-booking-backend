package com.moviebooking.controller;

import com.moviebooking.dto.CinemaRoomDTO;
import com.moviebooking.service.CinemaRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinema-rooms")
@RequiredArgsConstructor
public class CinemaRoomController {
    private final CinemaRoomService cinemaRoomService;

    @GetMapping("/cinema/{cinemaId}")
    public ResponseEntity<List<CinemaRoomDTO>> getRoomsByCinemaId(@PathVariable Integer cinemaId) {
        return ResponseEntity.ok(cinemaRoomService.getRoomsByCinemaId(cinemaId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CinemaRoomDTO> createCinemaRoom(@RequestBody CinemaRoomDTO roomDTO) {
        return ResponseEntity.ok(cinemaRoomService.createCinemaRoom(roomDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CinemaRoomDTO> updateCinemaRoom(@PathVariable Integer id, @RequestBody CinemaRoomDTO cinemaRoomDTO) {
        cinemaRoomDTO.setId(id);
        return ResponseEntity.ok(cinemaRoomService.updateCinemaRoom(cinemaRoomDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCinemaRoom(@PathVariable Integer id) {
        cinemaRoomService.deleteCinemaRoom(id);
        return ResponseEntity.noContent().build();
    }
}
