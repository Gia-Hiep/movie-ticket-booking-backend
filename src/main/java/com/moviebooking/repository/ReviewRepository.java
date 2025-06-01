package com.moviebooking.repository;

import com.moviebooking.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByMovieId(Integer movieId);
    Optional<Review> findByUserIdAndMovieId(Integer userId, Integer movieId);

    List<Review> findByUserId(Integer userId);
}