package com.payment.application.utilities;

import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayClient {

    public boolean charge(Double amount) {
        // simulate gateway response
        return Math.random() > 0.3;
    }
}
