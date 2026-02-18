package com.payment.application.utilities;

import com.payment.application.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class CardPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentMethod getMethod() {
        return PaymentMethod.CARD;
    }

    @Override
    public PaymentStatus processPayment(Payment payment) {

        // simulate gateway call
        System.out.println("Processing CARD payment...");

        return PaymentStatus.SUCCESS;
    }
}
