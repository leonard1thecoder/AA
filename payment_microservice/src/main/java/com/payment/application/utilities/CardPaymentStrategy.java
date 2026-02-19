package com.payment.application.utilities;

import com.payment.application.dtos.CardDetails;
import com.payment.application.entity.Payment;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class CardPaymentStrategy implements PaymentStrategy {

    private  CardDetails cardDetails;

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

    private CardDetails getCardDetails(){
        return this.cardDetails;
    }
}
