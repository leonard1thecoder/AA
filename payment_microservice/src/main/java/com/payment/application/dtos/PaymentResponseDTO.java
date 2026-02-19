package com.payment.application.dtos;

import com.payment.application.utilities.PaymentMethod;
import com.payment.application.utilities.PaymentStatus;
import com.utils.application.ResponseContract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO implements ResponseContract {

    private Long paymentId;

    private Long orderId;

    private Double amount;

    private PaymentMethod method;

    private PaymentStatus status;

    private String transactionReference;

    private String createdAt;
}
