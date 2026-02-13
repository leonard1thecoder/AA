package com.cart.application.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO {

    private Long companyProductId;
    private String productName;
    private String liters;
    private Double unitPrice;
    private Integer quantity;
    private Double totalPrice;
}
