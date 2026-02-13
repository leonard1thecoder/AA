package com.product.application.dto;

import com.utils.application.RequestContract;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class GetProductListByNameRequest implements RequestContract {
    private String productName;
}
