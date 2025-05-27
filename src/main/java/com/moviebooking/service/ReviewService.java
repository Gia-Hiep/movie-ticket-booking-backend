package com.moviebooking.service;

import com.moviebooking.dto.PromotionDTO;
import com.moviebooking.dto.ReviewDTO;
import com.moviebooking.entity.Promotion;
import com.moviebooking.entity.Review;
import com.moviebooking.entity.User;
import com.moviebooking.entity.Movie;
import com.moviebooking.repository.ReviewRepository;
import com.moviebooking.repository.UserRepository;
import com.moviebooking.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public List<ReviewDTO> getReviewsByMovieId(Integer movieId) {
        return reviewRepository.findByMovieId(movieId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        if (reviewRepository.findByUserIdAndMovieId(reviewDTO.getUserId(), reviewDTO.getMovieId()).isPresent()) {
            throw new RuntimeException("User already reviewed this movie");
        }
        User user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + reviewDTO.getUserId()));
        Movie movie = movieRepository.findById(reviewDTO.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + reviewDTO.getMovieId()));
        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review = reviewRepository.save(review);
        return mapToDTO(review);
    }

    private ReviewDTO mapToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setUserId(review.getUser().getId());
        dto.setMovieId(review.getMovie().getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }

    public ReviewDTO updateReview(ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewDTO.getId())
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewDTO.getId()));
        mapToEntity(reviewDTO, review);
        review = reviewRepository.save(review);
        return mapToDTO(review);
    }

    private void mapToEntity(ReviewDTO reviewDTO, Review review) {
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        User user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + reviewDTO.getUserId()));
        review.setUser(user);

        Movie movie = movieRepository.findById(reviewDTO.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + reviewDTO.getMovieId()));
        review.setMovie(movie);
    }

    public void deleteReview(Integer id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
        reviewRepository.delete(review);
    }
}