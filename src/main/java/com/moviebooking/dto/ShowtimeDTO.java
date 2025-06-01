package com.moviebooking.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShowtimeDTO {
    private Integer id;
    private MovieDTO movie;
    private CinemaRoomDTO cinemaRoom;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double ticketPrice;
    private String format;
    private String language;
    private boolean isActive;

    public boolean getIsActive() {
        return isActive;
    }
}