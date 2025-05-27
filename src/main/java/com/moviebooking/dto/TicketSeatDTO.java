package com.moviebooking.dto;

import lombok.Data;

@Data
public class TicketSeatDTO {
    private Integer id;
    private Integer ticketId;
    private Integer seatId;
    private Double price;
}
