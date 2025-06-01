package com.moviebooking.controller;

import com.moviebooking.dto.SeatDTO;
import com.moviebooking.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

//    @GetMapping("/cinema-room/{cinemaRoomId}")
//    public ResponseEntity<List<SeatDTO>> getSeatsByCinemaRoomId(@PathVariable Integer cinemaRoomId) {
//        return ResponseEntity.ok(seatService.getSeatsByCinemaRoomId(cinemaRoomId));
//    }
    @GetMapping("/cinema-room/{cinemaRoomId}")
    public ResponseEntity<List<SeatDTO>> getSeatsByCinemaRoomId(@PathVariable Integer cinemaRoomId, @RequestParam Integer showtimeId) {
        List<SeatDTO> seats = seatService.getSeatsByCinemaRoomId(cinemaRoomId, showtimeId);
        return ResponseEntity.ok(seats);
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SeatDTO> createSeat(@RequestBody SeatDTO seatDTO) {
        return ResponseEntity.ok(seatService.createSeat(seatDTO));
    }
}
