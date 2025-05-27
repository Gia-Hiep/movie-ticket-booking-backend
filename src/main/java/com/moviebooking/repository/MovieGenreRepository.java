package com.moviebooking.repository;

import com.moviebooking.entity.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieGenreRepository extends JpaRepository<MovieGenre, Integer> {
    List<MovieGenre> findByMovieId(Integer movieId);
    List<MovieGenre> findByGenreId(Integer genreId);
}