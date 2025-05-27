package com.moviebooking.controller;

import com.moviebooking.dto.CinemaDTO;
import com.moviebooking.service.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinemas")
@RequiredArgsConstructor
public class CinemaController {
    private final CinemaService cinemaService;

    @GetMapping
    public ResponseEntity<List<CinemaDTO>> getAllCinemas() {
        return ResponseEntity.ok(cinemaService.getAllCinemas());
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<CinemaDTO>> getCinemasByCity(@PathVariable String city) {
        return ResponseEntity.ok(cinemaService.getCinemasByCity(city));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CinemaDTO> createCinema(@RequestBody CinemaDTO cinemaDTO) {
        return ResponseEntity.ok(cinemaService.createCinema(cinemaDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CinemaDTO> updateCinema(@PathVariable Integer id, @RequestBody CinemaDTO cinemaDTO) {
        cinemaDTO.setId(id);
        return ResponseEntity.ok(cinemaService.updateCinema(cinemaDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCinema(@PathVariable Integer id) {
        cinemaService.deleteCinema(id);
        return ResponseEntity.noContent().build();
    }
}
