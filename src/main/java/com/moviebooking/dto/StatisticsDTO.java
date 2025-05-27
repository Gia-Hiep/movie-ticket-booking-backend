package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StatisticsDTO {
    private Integer id;
    private Integer showtimeId;
    private Integer movieId;
    private Integer cinemaId;
    private Double revenue;
    private Integer ticketCount;
    private LocalDate statsDate;
}
