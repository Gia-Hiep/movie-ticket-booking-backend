package com.moviebooking.service;

import com.moviebooking.dto.PaymentDTO;
import com.moviebooking.entity.Payment;
import com.moviebooking.entity.Ticket;
import com.moviebooking.repository.PaymentRepository;
import com.moviebooking.repository.TicketRepository;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;
    private final StripeService stripeService;
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public String createPaymentIntent(Double amount, String currency) throws StripeException {
        return stripeService.createPaymentIntent(amount, currency);
    }

    public PaymentDTO processPayment(PaymentDTO paymentDTO) {
        Ticket ticket = ticketRepository.findById(paymentDTO.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + paymentDTO.getTicketId()));
        Payment payment = new Payment();
        payment.setTicket(ticket);
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(Payment.PaymentMethod.valueOf(paymentDTO.getPaymentMethod()));
        payment.setTransactionId(paymentDTO.getTransactionId());
        payment.setStatus(paymentDTO.getPaymentMethod().equals("CASH") ? Payment.Status.PENDING : Payment.Status.COMPLETED);
        payment.setPaymentDetails(paymentDTO.getPaymentDetails());

        payment = paymentRepository.save(payment);
        return mapToDTO(payment);
    }

    private PaymentDTO mapToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setTicketId(payment.getTicket().getId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod().name());
        dto.setTransactionId(payment.getTransactionId());
        dto.setStatus(payment.getStatus().name());
        dto.setPaymentTime(payment.getPaymentTime());
        dto.setPaymentDetails(payment.getPaymentDetails());
        return dto;
    }
}