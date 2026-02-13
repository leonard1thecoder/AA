package com.cart.application.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {

    private Long cartId;
    private Long userId;
    private List<CartItemResponseDTO> items;
    private Double grandTotal;
}
