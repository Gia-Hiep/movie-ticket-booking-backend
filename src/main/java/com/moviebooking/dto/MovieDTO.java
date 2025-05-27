package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MovieDTO {
    private Integer id;
    private String title;
    private String description;
    private String posterUrl;
    private String trailerUrl;
    private Integer duration;
    private LocalDate releaseDate;
    private String director;
    private String cast;
    private Float rating;
    private boolean isActive;
}
