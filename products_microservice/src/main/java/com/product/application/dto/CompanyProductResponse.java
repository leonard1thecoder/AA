package com.product.application.dto;

import com.utils.application.ResponseContract;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyProductResponse implements ResponseContract {

    private Long companyProductId;
    private String companyName;
    private String productName;
    private Integer productQuantity;
    private Double productPrice;
    private Byte status;
    private String registrationDate;
    private String modifiedDate;
}

