package com.product.application.dto;

import com.utils.application.ResponseContract;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductListResponse implements ResponseContract {

    private long productId;

    private String productName

            ,productImagePath;

    private byte productStatus;
}
