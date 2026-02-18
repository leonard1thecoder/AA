package com.payment.application.service;

import com.cart.application.entities.Orders;
import com.cart.application.repositories.OrdersRepository;
import com.payment.application.entity.Payment;
import com.payment.application.repository.PaymentRepository;
import com.payment.application.utilities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;
    private final PaymentStrategyResolver resolver;

    public Payment pay(Long orderId, PaymentMethod method) {

        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalAmount())
                .method(method)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now().toString())
                .build();

        PaymentStrategy strategy = resolver.resolve(method);

        PaymentStatus status = strategy.processPayment(payment);

        payment.setStatus(status);
        payment.setTransactionReference(UUID.randomUUID().toString());

        return paymentRepository.save(payment);
    }
}

