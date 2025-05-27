package com.moviebooking.dto;

import lombok.Data;

@Data
public class MovieGenreDTO {
    private Integer id;
    private Integer movieId;
    private Integer genreId;
}
