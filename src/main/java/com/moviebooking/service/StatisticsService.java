package com.moviebooking.service;

import com.moviebooking.dto.StatisticsDTO;
import com.moviebooking.entity.Statistics;
import com.moviebooking.entity.Showtime;
import com.moviebooking.entity.Movie;
import com.moviebooking.entity.Cinema;
import com.moviebooking.repository.StatisticsRepository;
import com.moviebooking.repository.ShowtimeRepository;
import com.moviebooking.repository.MovieRepository;
import com.moviebooking.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;

    public List<StatisticsDTO> getStatisticsByDateRange(LocalDate start, LocalDate end) {
        return statisticsRepository.findByStatsDateBetween(start, end)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public StatisticsDTO createStatistics(StatisticsDTO statisticsDTO) {
        Statistics statistics = new Statistics();
        mapToEntity(statisticsDTO, statistics);
        statistics = statisticsRepository.save(statistics);
        return mapToDTO(statistics);
    }

    private StatisticsDTO mapToDTO(Statistics statistics) {
        StatisticsDTO dto = new StatisticsDTO();
        dto.setId(statistics.getId());
        dto.setShowtimeId(statistics.getShowtime() != null ? statistics.getShowtime().getId() : null);
        dto.setMovieId(statistics.getMovie() != null ? statistics.getMovie().getId() : null);
        dto.setCinemaId(statistics.getCinema() != null ? statistics.getCinema().getId() : null);
        dto.setRevenue(statistics.getRevenue());
        dto.setTicketCount(statistics.getTicketCount());
        dto.setStatsDate(statistics.getStatsDate());
        return dto;
    }

    private void mapToEntity(StatisticsDTO dto, Statistics statistics) {
        statistics.setShowtime(dto.getShowtimeId() != null ?
                showtimeRepository.findById(dto.getShowtimeId())
                        .orElseThrow(() -> new RuntimeException("Showtime not found with id: " + dto.getShowtimeId())) : null);
        statistics.setMovie(dto.getMovieId() != null ?
                movieRepository.findById(dto.getMovieId())
                        .orElseThrow(() -> new RuntimeException("Movie not found with id: " + dto.getMovieId())) : null);
        statistics.setCinema(dto.getCinemaId() != null ?
                cinemaRepository.findById(dto.getCinemaId())
                        .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + dto.getCinemaId())) : null);
        statistics.setRevenue(dto.getRevenue());
        statistics.setTicketCount(dto.getTicketCount());
        statistics.setStatsDate(dto.getStatsDate());
    }
}