package com.moviebooking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cinema_rooms")
@Data
public class CinemaRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoomType roomType = RoomType._2D;

    @Column(nullable = false)
    private boolean isActive = true;

    public enum RoomType {
        _2D, _3D, _4DX, IMAX, VIP
    }
}