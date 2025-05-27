package com.moviebooking.dto;

import lombok.Data;

@Data
public class UserPreferenceDTO {
    private Integer id;
    private Integer userId;
    private Integer genreId;
    private Integer preferenceLevel;
}
