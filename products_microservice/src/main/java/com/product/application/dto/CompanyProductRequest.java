package com.product.application.dto;

import com.utils.application.RequestContract;
import lombok.Data;

@Data
public class CompanyProductRequest implements RequestContract {

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
