package com.product.application.entities;

import com.retails.application.entity.RetailCompany;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Data
public class CompanyProducts {
    @Id
    private Long companyProductId;

    @ManyToOne
    @JoinColumn(name = "company_product_owner_id")
    private RetailCompany companyProduct_owner;

    @ManyToOne
    @JoinColumn(name = "product_list_product_id")
    private ProductList productList;

    @ManyToOne
    @JoinColumn(name = "products_liters_id")
    private ProductsLiters productsLiters;

    private Integer productQuantity;

    private String  registrationDate;

    private String modifiedDate;

    //if status 1 means the product to buy.
    //if 0 means no product to buy, quantity is 0.
    private Byte companyProductStatus;

    private Double productPrice;

    private Double returnableBottlePrice,lateNightPriceIncrease;

    private byte lateNightPriceStatus,returnableBottleStatus;
}
