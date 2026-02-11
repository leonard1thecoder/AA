package com.product.application.dto;

import com.utils.application.ResponseContract;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductLitersResponse implements ResponseContract {
    private String productLiters;

    private String registeredDate;

    private Byte status;

    private String administratorName;
}
