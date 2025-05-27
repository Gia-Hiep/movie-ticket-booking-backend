package com.moviebooking.service;

import com.moviebooking.dto.TicketDTO;
import com.moviebooking.entity.Ticket;
import com.moviebooking.entity.User;
import com.moviebooking.entity.Showtime;
import com.moviebooking.repository.TicketRepository;
import com.moviebooking.repository.UserRepository;
import com.moviebooking.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.moviebooking.service.ShowtimeService;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ShowtimeRepository showtimeRepository;
    private final QRCodeService qrCodeService;
    private final ShowtimeService showtimeService;

    public TicketDTO bookTicket(TicketDTO ticketDTO) {
        User user = userRepository.findById(ticketDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + ticketDTO.getUserId()));
        Showtime showtime = showtimeRepository.findById(ticketDTO.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Showtime not found with id: " + ticketDTO.getShowtimeId()));
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setShowtime(showtime);
        ticket.setTotalAmount(ticketDTO.getTotalAmount());
        ticket.setQrCode(qrCodeService.generateQRCode(ticketDTO));
        ticket = ticketRepository.save(ticket);

        // Bỏ gửi email/SMS xác nhận, QR code được trả qua API
        return mapToDTO(ticket);
    }

    public List<TicketDTO> getUserTickets(Integer userId) {
        return ticketRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private TicketDTO mapToDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setUserId(ticket.getUser().getId());
        dto.setShowtimeId(ticket.getShowtime().getId());
        dto.setShowtime(showtimeService.mapToDTO(ticket.getShowtime()));
        dto.setQrCode(ticket.getQrCode());
        dto.setTotalAmount(ticket.getTotalAmount());
        dto.setStatus(ticket.getStatus().name());
        dto.setPurchaseTime(ticket.getPurchaseTime());
        return dto;
    }
}