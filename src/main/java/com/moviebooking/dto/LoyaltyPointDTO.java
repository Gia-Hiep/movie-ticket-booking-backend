package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoyaltyPointDTO {
    private Integer id;
    private Integer userId;
    private Integer points;
    private LocalDateTime earnedDate;
    private LocalDateTime usedDate;
    private String transactionType;
    private String transactionDetails;
}
