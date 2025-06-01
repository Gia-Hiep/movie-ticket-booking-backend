package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Integer id;
    private Integer userId;
    private UserDTO user;
    private MovieDTO movie;
    private Integer movieId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
