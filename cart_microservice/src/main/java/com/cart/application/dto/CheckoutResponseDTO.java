package com.cart.application.dto;

import com.utils.application.ResponseContract;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutResponseDTO implements ResponseContract {

    private Long orderId;
    private Double totalAmount;
    private String status;
    private String orderDate;
}
