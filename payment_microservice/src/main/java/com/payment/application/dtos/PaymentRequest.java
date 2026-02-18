package com.payment.application.dtos;

import com.payment.application.utilities.PaymentMethod;
import lombok.Data;

@Data
public class PaymentRequest {

    private Long orderId;
    private PaymentMethod paymentMethod;
    private CardDetails cardDetails;
}
