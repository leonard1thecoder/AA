package com.payment.application.dtos;

import com.payment.application.utilities.PaymentMethod;
import com.utils.application.RequestContract;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequest implements RequestContract {

    private Long orderId;
    private PaymentMethod paymentMethod;
    private CardDetails cardDetails;
}
