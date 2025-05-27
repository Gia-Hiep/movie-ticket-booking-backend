package com.moviebooking.service;

import com.moviebooking.dto.TicketSeatDTO;
import com.moviebooking.entity.TicketSeat;
import com.moviebooking.entity.Ticket;
import com.moviebooking.entity.Seat;
import com.moviebooking.repository.TicketSeatRepository;
import com.moviebooking.repository.TicketRepository;
import com.moviebooking.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketSeatService {
    private final TicketSeatRepository ticketSeatRepository;
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    public List<TicketSeatDTO> getSeatsByTicketId(Integer ticketId) {
        return ticketSeatRepository.findByTicketId(ticketId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TicketSeatDTO createTicketSeat(TicketSeatDTO ticketSeatDTO) {
        Ticket ticket = ticketRepository.findById(ticketSeatDTO.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketSeatDTO.getTicketId()));
        Seat seat = seatRepository.findById(ticketSeatDTO.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + ticketSeatDTO.getSeatId()));
        TicketSeat ticketSeat = new TicketSeat();
        ticketSeat.setTicket(ticket);
        ticketSeat.setSeat(seat);
        ticketSeat.setPrice(ticketSeatDTO.getPrice());
        ticketSeat = ticketSeatRepository.save(ticketSeat);
        return mapToDTO(ticketSeat);
    }

    private TicketSeatDTO mapToDTO(TicketSeat ticketSeat) {
        TicketSeatDTO dto = new TicketSeatDTO();
        dto.setId(ticketSeat.getId());
        dto.setTicketId(ticketSeat.getTicket().getId());
        dto.setSeatId(ticketSeat.getSeat().getId());
        dto.setPrice(ticketSeat.getPrice());
        return dto;
    }
}