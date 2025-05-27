package com.moviebooking.controller;

import com.moviebooking.dto.MovieGenreDTO;
import com.moviebooking.service.MovieGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie-genres")
@RequiredArgsConstructor
public class MovieGenreController {
    private final MovieGenreService movieGenreService;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<MovieGenreDTO>> getGenresByMovieId(@PathVariable Integer movieId) {
        return ResponseEntity.ok(movieGenreService.getGenresByMovieId(movieId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieGenreDTO> createMovieGenre(@RequestBody MovieGenreDTO movieGenreDTO) {
        return ResponseEntity.ok(movieGenreService.createMovieGenre(movieGenreDTO));
    }
}
