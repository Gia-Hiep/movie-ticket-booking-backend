package com.moviebooking.service;

import com.moviebooking.dto.MovieDTO;
import com.moviebooking.entity.Movie;
import com.moviebooking.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public List<MovieDTO> getAllMovies() {
        return movieRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public MovieDTO getMovieById(Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return mapToDTO(movie);
    }

    public List<MovieDTO> searchMovies(String query) {
        return movieRepository.searchMovies(query)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public MovieDTO createMovie(MovieDTO movieDTO) {
        Movie movie = new Movie();
        mapToEntity(movieDTO, movie);
        movie = movieRepository.save(movie);
        return mapToDTO(movie);
    }

    public MovieDTO updateMovie(Integer id, MovieDTO movieDTO) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        mapToEntity(movieDTO, movie);
        movie = movieRepository.save(movie);
        return mapToDTO(movie);
    }

    public void deleteMovie(Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        movie.setActive(false);

        movieRepository.save(movie);
    }

    private MovieDTO mapToDTO(Movie movie) {
        MovieDTO dto = new MovieDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDescription(movie.getDescription());
        dto.setPosterUrl(movie.getPosterUrl());
        dto.setTrailerUrl(movie.getTrailerUrl());
        dto.setDuration(movie.getDuration());
        dto.setReleaseDate(movie.getReleaseDate());
        dto.setDirector(movie.getDirector());
        dto.setCast(movie.getCast());
        dto.setRating(movie.getRating());
        dto.setActive(movie.isActive());
        return dto;
    }

    private void mapToEntity(MovieDTO dto, Movie movie) {
        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getDescription());
        movie.setPosterUrl(dto.getPosterUrl());
        movie.setTrailerUrl(dto.getTrailerUrl());
        movie.setDuration(dto.getDuration());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setDirector(dto.getDirector());
        movie.setCast(dto.getCast());
        movie.setRating(dto.getRating());
        movie.setActive(dto.getIsActive());
    }
}
