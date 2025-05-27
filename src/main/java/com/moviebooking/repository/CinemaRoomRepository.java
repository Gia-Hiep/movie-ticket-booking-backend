package com.moviebooking.repository;

import com.moviebooking.entity.CinemaRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaRoomRepository extends JpaRepository<CinemaRoom, Integer> {
    List<CinemaRoom> findByCinemaIdAndIsActiveTrue(Integer cinemaId);
}