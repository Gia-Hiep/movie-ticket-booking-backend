package com.moviebooking.dto;

import lombok.Data;

@Data
public class CinemaDTO {
    private Integer id;
    private String name;
    private String address;
    private String city;
    private String phone;
    private String description;
    private String imageUrl;
    private boolean isActive;

}
