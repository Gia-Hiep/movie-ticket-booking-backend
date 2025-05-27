package com.moviebooking.dto;

import lombok.Data;

@Data
public class SeatDTO {
    private Integer id;
    private Integer cinemaRoomId;
    private String seatNumber;
    private String seatRow;
    private String seatType;
    private Double priceMultiplier;
    private boolean isActive;
}
