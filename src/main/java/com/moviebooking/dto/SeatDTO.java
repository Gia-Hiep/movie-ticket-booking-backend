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
    private boolean isBooked;

    public boolean getIsActive(){
        return isActive;
    }

    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }
    public void setIsBooked(boolean isBooked){
        this.isBooked = isBooked;
    }

    public boolean getIsBooked(){
        return isBooked;
    }

}
