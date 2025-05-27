package com.moviebooking.repository;

import com.moviebooking.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    List<Movie> findByIsActiveTrue();

    @Query("SELECT m FROM Movie m WHERE m.title LIKE %:query% OR m.description LIKE %:query% " +
            "OR m.director LIKE %:query% OR m.cast LIKE %:query%")
    List<Movie> searchMovies(String query);
}