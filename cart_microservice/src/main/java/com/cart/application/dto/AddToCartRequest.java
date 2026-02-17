package com.cart.application.dto;

import com.utils.application.RequestContract;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddToCartRequest implements RequestContract {
    private Long userId;
    private Long companyProductId;
    private Integer quantity;
}
