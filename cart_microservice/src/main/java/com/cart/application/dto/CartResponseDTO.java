package com.cart.application.dto;

import com.utils.application.ResponseContract;
import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO implements ResponseContract {

    private Long cartId;
    private Long userId;
    private List<CartItemResponseDTO> items;
    private Double grandTotal;
}
