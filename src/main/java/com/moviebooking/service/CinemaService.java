package com.moviebooking.service;

import com.moviebooking.dto.CinemaDTO;
import com.moviebooking.entity.Cinema;
import com.moviebooking.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaService {
    private final CinemaRepository cinemaRepository;

    public List<CinemaDTO> getAllCinemas() {
        return cinemaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<CinemaDTO> getCinemasByCity(String city) {
        return cinemaRepository.findByCityAndIsActiveTrue(city)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CinemaDTO createCinema(CinemaDTO cinemaDTO) {
        Cinema cinema = new Cinema();
        mapToEntity(cinemaDTO, cinema);
        cinema = cinemaRepository.save(cinema);
        return mapToDTO(cinema);
    }

    private CinemaDTO mapToDTO(Cinema cinema) {
        CinemaDTO dto = new CinemaDTO();
        dto.setId(cinema.getId());
        dto.setName(cinema.getName());
        dto.setAddress(cinema.getAddress());
        dto.setCity(cinema.getCity());
        dto.setPhone(cinema.getPhone());
        dto.setDescription(cinema.getDescription());
        dto.setImageUrl(cinema.getImageUrl());
        dto.setActive(cinema.isActive());
        return dto;
    }

    private void mapToEntity(CinemaDTO dto, Cinema cinema) {
        cinema.setName(dto.getName());
        cinema.setAddress(dto.getAddress());
        cinema.setCity(dto.getCity());
        cinema.setPhone(dto.getPhone());
        cinema.setDescription(dto.getDescription());
        cinema.setImageUrl(dto.getImageUrl());
        cinema.setActive(dto.isActive());
    }

    public CinemaDTO updateCinema(CinemaDTO cinemaDTO) {
        Cinema cinema = cinemaRepository.findById(cinemaDTO.getId())
                .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + cinemaDTO.getId()));
        mapToEntity(cinemaDTO, cinema);
        cinema = cinemaRepository.save(cinema);
        return mapToDTO(cinema);
    }

    public void deleteCinema(Integer id) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + id));
        cinemaRepository.delete(cinema);
    }
}
