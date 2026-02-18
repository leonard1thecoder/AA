package com.payment.application.controller;

import com.payment.application.entity.Payment;
import com.payment.application.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/process/{orderId}")
    public ResponseEntity<Payment> processPayment(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.pay(orderId,null));
    }
}
