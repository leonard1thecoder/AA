package com.product.application.dto;

import lombok.Data;

@Data
public class CompanyProductRequest {

    private Long companyId;
    private Long productListId;
    private Long productsLitersId;

    private Integer productQuantity;
    private Double productPrice;

    private Double returnableBottlePrice;
    private Double lateNightPriceIncrease;

    private Byte companyProductStatus;
    private byte lateNightPriceStatus;
}
