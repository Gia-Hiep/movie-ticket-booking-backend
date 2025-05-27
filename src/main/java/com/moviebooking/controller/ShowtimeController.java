package com.moviebooking.controller;

import com.moviebooking.dto.ShowtimeDTO;
import com.moviebooking.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/showtimes")
@RequiredArgsConstructor
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowtimeDTO>> getShowtimesByMovieId(@PathVariable Integer movieId) {
        return ResponseEntity.ok(showtimeService.getShowtimesByMovieId(movieId));
    }

    @GetMapping("/{showtimeId}")
    public ResponseEntity<ShowtimeDTO> getShowtimeById(@PathVariable Integer showtimeId) {
        return ResponseEntity.ok(showtimeService.getShowtimeById(showtimeId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowtimeDTO> createShowtime(@RequestBody ShowtimeDTO showtimeDTO) {
        return ResponseEntity.ok(showtimeService.createShowtime(showtimeDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowtimeDTO> updateShowtime(@PathVariable Integer id, @RequestBody ShowtimeDTO showtimeDTO) {
        showtimeDTO.setId(id);
        return ResponseEntity.ok(showtimeService.updateShowtime(showtimeDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Integer id) {
        showtimeService.deleteShowtime(id);
        return ResponseEntity.noContent().build();
    }
}
