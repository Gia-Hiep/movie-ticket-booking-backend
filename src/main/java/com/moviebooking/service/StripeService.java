package com.moviebooking.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    public StripeService(@Value("${stripe.api-key}") String apiKey) {
        Stripe.apiKey = apiKey;
    }

    public String createPaymentIntent(Double amount, String currency) throws StripeException {
        if (currency.equalsIgnoreCase("vnd") && amount > 10000000000L) {
            throw new IllegalArgumentException("Số tiền vượt quá giới hạn 10 tỷ VND");
        }
        long stripeAmount = currency.equalsIgnoreCase("vnd") ? amount.longValue() : (long) (amount * 100);
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(stripeAmount)
                .setCurrency(currency)
                .addPaymentMethodType("card")
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return paymentIntent.getClientSecret();
    }
}