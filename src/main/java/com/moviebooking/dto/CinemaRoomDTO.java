package com.moviebooking.dto;

import lombok.Data;

@Data
public class CinemaRoomDTO {
    private Integer id;
    private CinemaDTO cinema;
    private String name;
    private Integer capacity;
    private String roomType;
    private boolean isActive;

    public boolean getIsActive(){
        return isActive;
    }
}