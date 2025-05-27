package com.moviebooking.service;

import com.moviebooking.dto.MovieGenreDTO;
import com.moviebooking.entity.Genre;
import com.moviebooking.entity.Movie;
import com.moviebooking.entity.MovieGenre;
import com.moviebooking.repository.GenreRepository;
import com.moviebooking.repository.MovieGenreRepository;
import com.moviebooking.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieGenreService {
    private final MovieGenreRepository movieGenreRepository;
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    public List<MovieGenreDTO> getGenresByMovieId(Integer movieId) {
        return movieGenreRepository.findByMovieId(movieId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public MovieGenreDTO createMovieGenre(MovieGenreDTO movieGenreDTO) {
        Movie movie = movieRepository.findById(movieGenreDTO.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + movieGenreDTO.getMovieId()));
        Genre genre = genreRepository.findById(movieGenreDTO.getGenreId())
                .orElseThrow(() -> new RuntimeException("Genre not found with id: " + movieGenreDTO.getGenreId()));
        MovieGenre movieGenre = new MovieGenre();
        movieGenre.setMovie(movie);
        movieGenre.setGenre(genre);
        movieGenre = movieGenreRepository.save(movieGenre);
        return mapToDTO(movieGenre);
    }

    private MovieGenreDTO mapToDTO(MovieGenre movieGenre) {
        MovieGenreDTO dto = new MovieGenreDTO();
        dto.setId(movieGenre.getId());
        dto.setMovieId(movieGenre.getMovie().getId());
        dto.setGenreId(movieGenre.getGenre().getId());
        return dto;
    }
}