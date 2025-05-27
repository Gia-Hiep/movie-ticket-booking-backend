package com.moviebooking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    private String phoneNumber;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private MembershipLevel membershipLevel;

    @Column(nullable = false)
    private boolean isActive;

    @Column(length = 255)
    private String deviceToken;

    public enum Role {
        USER, ADMIN
    }

    public enum MembershipLevel {
        STANDARD, PREMIUM, VIP
    }
}