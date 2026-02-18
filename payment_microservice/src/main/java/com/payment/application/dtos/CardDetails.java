package com.payment.application.dtos;

import lombok.Data;

@Data
public class CardDetails {

    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private String cvc;
    private String cardHolderName;
}
