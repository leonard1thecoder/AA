package com.payment.application.entity;

import com.cart.application.entities.Orders;
import com.payment.application.utilities.PaymentMethod;
import com.payment.application.utilities.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne
    private Orders order;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String transactionReference;

    private String createdAt;
}
