package com.moviebooking.controller;

import com.moviebooking.dto.TicketDTO;
import com.moviebooking.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TicketDTO> bookTicket(@RequestBody TicketDTO ticketDTO) {
        return ResponseEntity.ok(ticketService.bookTicket(ticketDTO));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TicketDTO>> getUserTickets(@PathVariable Integer userId) {
        return ResponseEntity.ok(ticketService.getUserTickets(userId));
    }
}
