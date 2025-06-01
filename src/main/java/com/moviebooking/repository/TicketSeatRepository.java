package com.moviebooking.repository;

import com.moviebooking.entity.TicketSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketSeatRepository extends JpaRepository<TicketSeat, Integer> {
    List<TicketSeat> findByTicketId(Integer ticketId);

    @Query("SELECT ts FROM TicketSeat ts WHERE ts.ticket.showtime.id = :showtimeId")
    List<TicketSeat> findByShowtimeId(Integer showtimeId);
}