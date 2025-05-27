package com.moviebooking.controller;

import com.moviebooking.dto.TicketSeatDTO;
import com.moviebooking.service.TicketSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket-seats")
@RequiredArgsConstructor
public class TicketSeatController {
    private final TicketSeatService ticketSeatService;

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<TicketSeatDTO>> getSeatsByTicketId(@PathVariable Integer ticketId) {
        return ResponseEntity.ok(ticketSeatService.getSeatsByTicketId(ticketId));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TicketSeatDTO> createTicketSeat(@RequestBody TicketSeatDTO ticketSeatDTO) {
        return ResponseEntity.ok(ticketSeatService.createTicketSeat(ticketSeatDTO));
    }
}
