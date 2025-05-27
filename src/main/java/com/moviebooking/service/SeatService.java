package com.moviebooking.service;

import com.moviebooking.dto.SeatDTO;
import com.moviebooking.entity.CinemaRoom;
import com.moviebooking.entity.Seat;
import com.moviebooking.repository.CinemaRoomRepository;
import com.moviebooking.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final CinemaRoomRepository cinemaRoomRepository;

    public List<SeatDTO> getSeatsByCinemaRoomId(Integer cinemaRoomId) {
        return seatRepository.findByCinemaRoomIdAndIsActiveTrue(cinemaRoomId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SeatDTO createSeat(SeatDTO seatDTO) {
        Seat seat = new Seat();
        mapToEntity(seatDTO, seat);
        seat = seatRepository.save(seat);
        return mapToDTO(seat);
    }

    private SeatDTO mapToDTO(Seat seat) {
        SeatDTO dto = new SeatDTO();
        dto.setId(seat.getId());
        dto.setCinemaRoomId(seat.getCinemaRoom().getId());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setSeatRow(seat.getSeatRow());
        dto.setSeatType(seat.getSeatType().name());
        dto.setPriceMultiplier(seat.getPriceMultiplier());
        dto.setActive(seat.isActive());
        return dto;
    }

    private void mapToEntity(SeatDTO dto, Seat seat) {
        CinemaRoom cinemaRoom = cinemaRoomRepository.findById(dto.getCinemaRoomId())
                .orElseThrow(() -> new RuntimeException("Cinema room not found with id: " + dto.getCinemaRoomId()));
        seat.setCinemaRoom(cinemaRoom);
        seat.setSeatNumber(dto.getSeatNumber());
        seat.setSeatRow(dto.getSeatRow());
        seat.setSeatType(Seat.SeatType.valueOf(dto.getSeatType()));
        seat.setPriceMultiplier(dto.getPriceMultiplier());
        seat.setActive(dto.isActive());
    }
}