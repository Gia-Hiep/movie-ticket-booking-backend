package com.moviebooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketDTO {
    private Integer id;
    private Integer userId;
    private Integer showtimeId;
    private ShowtimeDTO showtime;
    private String qrCode;
    private Double totalAmount;
    private String status;
    private LocalDateTime purchaseTime;
}
