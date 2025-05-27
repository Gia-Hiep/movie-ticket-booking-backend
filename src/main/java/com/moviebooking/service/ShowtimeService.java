package com.moviebooking.service;

import com.moviebooking.dto.CinemaDTO;
import com.moviebooking.dto.CinemaRoomDTO;
import com.moviebooking.dto.MovieDTO;
import com.moviebooking.dto.ShowtimeDTO;
import com.moviebooking.entity.Cinema;
import com.moviebooking.entity.CinemaRoom;
import com.moviebooking.entity.Movie;
import com.moviebooking.entity.Showtime;
import com.moviebooking.repository.CinemaRoomRepository;
import com.moviebooking.repository.MovieRepository;
import com.moviebooking.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final CinemaRoomRepository cinemaRoomRepository;

    public List<ShowtimeDTO> getShowtimesByMovieId(Integer movieId) {
        return showtimeRepository.findByMovieIdAndIsActiveTrue(movieId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ShowtimeDTO getShowtimeById(Integer showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found with id: " + showtimeId));
        return mapToDTO(showtime);
    }

    public ShowtimeDTO createShowtime(ShowtimeDTO showtimeDTO) {
        Showtime showtime = new Showtime();
        mapToEntity(showtimeDTO, showtime);
        showtime = showtimeRepository.save(showtime);
        return mapToDTO(showtime);
    }

    public ShowtimeDTO updateShowtime(ShowtimeDTO showtimeDTO) {
        Showtime showtime = showtimeRepository.findById(showtimeDTO.getId())
                .orElseThrow(() -> new RuntimeException("Showtime not found with id: " + showtimeDTO.getId()));
        mapToEntity(showtimeDTO, showtime);
        showtime = showtimeRepository.save(showtime);
        return mapToDTO(showtime);
    }

    public void deleteShowtime(Integer id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found with id: " + id));
        showtimeRepository.delete(showtime);
    }

    public ShowtimeDTO mapToDTO(Showtime showtime) {
        ShowtimeDTO dto = new ShowtimeDTO();
        dto.setId(showtime.getId());

        // Populate MovieDTO
        Movie movie = showtime.getMovie();
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movie.getId());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setDescription(movie.getDescription());
        movieDTO.setPosterUrl(movie.getPosterUrl());
        movieDTO.setTrailerUrl(movie.getTrailerUrl());
        movieDTO.setDuration(movie.getDuration());
        movieDTO.setReleaseDate(movie.getReleaseDate());
        movieDTO.setDirector(movie.getDirector());
        movieDTO.setCast(movie.getCast());
        movieDTO.setRating(movie.getRating());
        movieDTO.setActive(movie.isActive());
        dto.setMovie(movieDTO);

        // Populate CinemaRoomDTO
        CinemaRoom cinemaRoom = showtime.getCinemaRoom();
        CinemaRoomDTO cinemaRoomDTO = new CinemaRoomDTO();
        cinemaRoomDTO.setId(cinemaRoom.getId());
        cinemaRoomDTO.setName(cinemaRoom.getName());
        cinemaRoomDTO.setCapacity(cinemaRoom.getCapacity());
        cinemaRoomDTO.setRoomType(cinemaRoom.getRoomType().name());
        cinemaRoomDTO.setActive(cinemaRoom.isActive());

        // Populate CinemaDTO
        Cinema cinema = cinemaRoom.getCinema();
        CinemaDTO cinemaDTO = new CinemaDTO();
        cinemaDTO.setId(cinema.getId());
        cinemaDTO.setName(cinema.getName());
        cinemaDTO.setAddress(cinema.getAddress());
        cinemaDTO.setCity(cinema.getCity());
        cinemaDTO.setPhone(cinema.getPhone());
        cinemaDTO.setDescription(cinema.getDescription());
        cinemaDTO.setImageUrl(cinema.getImageUrl());
        cinemaDTO.setActive(cinema.isActive());
        cinemaRoomDTO.setCinema(cinemaDTO);

        dto.setCinemaRoom(cinemaRoomDTO);
        dto.setStartTime(showtime.getStartTime());
        dto.setEndTime(showtime.getEndTime());
        dto.setTicketPrice(showtime.getTicketPrice());
        dto.setFormat(showtime.getFormat().name());
        dto.setLanguage(showtime.getLanguage());
        dto.setActive(showtime.isActive());
        return dto;
    }

    private void mapToEntity(ShowtimeDTO dto, Showtime showtime) {
        Movie movie = movieRepository.findById(dto.getMovie().getId())
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + dto.getMovie().getId()));
        CinemaRoom cinemaRoom = cinemaRoomRepository.findById(dto.getCinemaRoom().getId())
                .orElseThrow(() -> new RuntimeException("Cinema room not found with id: " + dto.getCinemaRoom().getId()));
        showtime.setMovie(movie);
        showtime.setCinemaRoom(cinemaRoom);
        showtime.setStartTime(dto.getStartTime());
        showtime.setEndTime(dto.getEndTime());
        showtime.setTicketPrice(dto.getTicketPrice());
        showtime.setFormat(Showtime.Format.valueOf(dto.getFormat()));
        showtime.setLanguage(dto.getLanguage());
        showtime.setActive(dto.isActive());
    }
}