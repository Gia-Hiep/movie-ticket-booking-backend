package com.moviebooking.repository;

import com.moviebooking.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Integer> {
    List<Showtime> findByMovieIdAndIsActiveTrue(Integer movieId);
    List<Showtime> findByCinemaRoomIdAndIsActiveTrue(Integer cinemaRoomId);
    List<Showtime> findByStartTimeBetweenAndIsActiveTrue(LocalDateTime start, LocalDateTime end);

    List<Showtime> findByMovieId(Integer movieId);
}