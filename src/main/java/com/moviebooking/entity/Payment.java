package com.moviebooking.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "ticket_id", nullable = false, unique = true)
    private Ticket ticket;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Column(length = 100)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status status = Status.PENDING;

    @Column(nullable = false)
    private LocalDateTime paymentTime = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String paymentDetails;

    public enum PaymentMethod {
        CREDIT_CARD, MOMO, ZALOPAY, BANKING, CASH
    }

    public enum Status {
        PENDING, COMPLETED, FAILED, REFUNDED
    }
}