package com.payment.application.utilities;

import com.payment.application.entity.Payment;

public interface PaymentStrategy {

    PaymentMethod getMethod();

    PaymentStatus processPayment(Payment payment);
}
