package com.moviebooking.service;

import com.moviebooking.dto.CinemaDTO;
import com.moviebooking.dto.CinemaRoomDTO;
import com.moviebooking.entity.Cinema;
import com.moviebooking.entity.CinemaRoom;
import com.moviebooking.repository.CinemaRepository;
import com.moviebooking.repository.CinemaRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaRoomService {
    private final CinemaRoomRepository cinemaRoomRepository;
    private final CinemaRepository cinemaRepository;

    public List<CinemaRoomDTO> getRoomsByCinemaId(Integer cinemaId) {
        return cinemaRoomRepository.findByCinemaIdAndIsActiveTrue(cinemaId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CinemaRoomDTO createCinemaRoom(CinemaRoomDTO roomDTO) {
        CinemaRoom room = new CinemaRoom();
        mapToEntity(roomDTO, room);
        room = cinemaRoomRepository.save(room);
        return mapToDTO(room);
    }

    public CinemaRoomDTO updateCinemaRoom(CinemaRoomDTO cinemaRoomDTO) {
        CinemaRoom cinemaRoom = cinemaRoomRepository.findById(cinemaRoomDTO.getId())
                .orElseThrow(() -> new RuntimeException("Cinema room not found with id: " + cinemaRoomDTO.getId()));
        mapToEntity(cinemaRoomDTO, cinemaRoom);
        cinemaRoom = cinemaRoomRepository.save(cinemaRoom);
        return mapToDTO(cinemaRoom);
    }

    public void deleteCinemaRoom(Integer id) {
        CinemaRoom cinemaRoom = cinemaRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cinema room not found with id: " + id));
        cinemaRoomRepository.delete(cinemaRoom);
    }

    private CinemaRoomDTO mapToDTO(CinemaRoom room) {
        CinemaRoomDTO dto = new CinemaRoomDTO();
        dto.setId(room.getId());

        // Populate CinemaDTO
        Cinema cinema = room.getCinema();
        CinemaDTO cinemaDTO = new CinemaDTO();
        cinemaDTO.setId(cinema.getId());
        cinemaDTO.setName(cinema.getName());
        cinemaDTO.setAddress(cinema.getAddress());
        cinemaDTO.setCity(cinema.getCity());
        cinemaDTO.setPhone(cinema.getPhone());
        cinemaDTO.setDescription(cinema.getDescription());
        cinemaDTO.setImageUrl(cinema.getImageUrl());
        cinemaDTO.setActive(cinema.isActive());
        dto.setCinema(cinemaDTO);

        dto.setName(room.getName());
        dto.setCapacity(room.getCapacity());
        dto.setRoomType(room.getRoomType().name());
        dto.setActive(room.isActive());
        return dto;
    }

    private void mapToEntity(CinemaRoomDTO dto, CinemaRoom room) {
        Cinema cinema = cinemaRepository.findById(dto.getCinema().getId())
                .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + dto.getCinema().getId()));
        room.setCinema(cinema);
        room.setName(dto.getName());
        room.setCapacity(dto.getCapacity());
        room.setRoomType(CinemaRoom.RoomType.valueOf(dto.getRoomType()));
        room.setActive(dto.getIsActive());
    }
}