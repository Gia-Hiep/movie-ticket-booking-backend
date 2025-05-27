package com.moviebooking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "seats")
@Data
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cinema_room_id", nullable = false)
    private CinemaRoom cinemaRoom;

    @Column(nullable = false, length = 10)
    private String seatNumber;

    @Column(nullable = false, length = 5)
    private String seatRow;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SeatType seatType = SeatType.STANDARD;

    @Column(nullable = false)
    private Double priceMultiplier = 1.0;

    @Column(nullable = false)
    private boolean isActive = true;

    public enum SeatType {
        STANDARD, VIP, COUPLE, PREMIUM
    }
}