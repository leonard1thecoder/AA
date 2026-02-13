package com.cart.application.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutResponseDTO {

    private Long orderId;
    private Double totalAmount;
    private String status;
    private String orderDate;
}
