package com.moviebooking.repository;

import com.moviebooking.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Integer> {
    List<Statistics> findByMovieId(Integer movieId);
    List<Statistics> findByCinemaId(Integer cinemaId);
    List<Statistics> findByStatsDateBetween(LocalDate start, LocalDate end);
}