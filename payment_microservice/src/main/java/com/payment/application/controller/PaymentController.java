package com.payment.application.controller;

import com.payment.application.dtos.PaymentRequest;
import com.payment.application.dtos.PaymentResponseDTO;
import com.payment.application.executor.PaymentServiceExecutor;
import com.payment.application.service.PaymentService;
import com.utils.application.ResponseContract;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Executors;

@RestController

@RequestMapping("/api/payments")
public class PaymentController extends PaymentServiceExecutor {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        super(Executors.newVirtualThreadPerTaskExecutor());
        this.paymentService = paymentService;
    }

    @PostMapping("/process/{orderId}")
    public ResponseEntity<List<? extends ResponseContract>> processPayment(@PathVariable Long orderId, PaymentRequest request) {
        request.setOrderId(orderId);
        var response = super.executeService(paymentService, request, "pay");

        if (response.getFirst() instanceof PaymentResponseDTO) {
            return ResponseEntity.status(201).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
