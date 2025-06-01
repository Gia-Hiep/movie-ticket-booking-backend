package com.moviebooking.controller;

import com.moviebooking.dto.ReviewDTO;
import com.moviebooking.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByMovieId(@PathVariable Integer movieId) {
        return ResponseEntity.ok(reviewService.getReviewsByMovieId(movieId));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.createReview(reviewDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Integer id, @RequestBody ReviewDTO reviewDTO) {
        reviewDTO.setId(id);
        return ResponseEntity.ok(reviewService.updateReview(reviewDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
