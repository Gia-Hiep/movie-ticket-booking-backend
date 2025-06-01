package com.moviebooking.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String fullName;
    private String role;
    private String membershipLevel;
    private boolean isActive;

    public boolean getIsActive(){
        return isActive;
    }
}