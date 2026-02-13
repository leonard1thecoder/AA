package com.product.application.controllers;

import com.product.application.dto.GetProductListByNameRequest;
import com.product.application.dto.RegisterProductListRequest;
import com.product.application.executor.ProductServiceContractExecutor;
import com.product.application.services.ProductServicesListService;
import com.utils.application.ResponseContract;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("")
@RestController
public class ProductListController {
    private final ProductServicesListService productServicesListService;
    private final ProductServiceContractExecutor serviceExecutor;

    public ProductListController(ProductServicesListService productServicesListService) {
        this.productServicesListService = productServicesListService;
        this.serviceExecutor = ProductServiceContractExecutor.getInstance();
    }

    @GetMapping("getAllProductLiter")
    private ResponseEntity<List<? extends ResponseContract>> getAllProductLists() {
        var response = serviceExecutor.buildContactFormServiceExecutor(productServicesListService, null, "getAllProductLiters");
        return ResponseEntity.ok(response);
    }
    @GetMapping("getAllProductLiter")
    private ResponseEntity<List<? extends ResponseContract>> addProductList(RegisterProductListRequest request) {
        var response = serviceExecutor.buildContactFormServiceExecutor(productServicesListService, request, "getAllProductLiters");
        return ResponseEntity.ok(response);
    }
    @GetMapping("getProductListByName/{productListName}")
    private ResponseEntity<List<? extends ResponseContract>> getProductListByName(@PathVariable String productListName) {
        var response = serviceExecutor.buildContactFormServiceExecutor(productServicesListService, GetProductListByNameRequest
                .builder()
                        .productName(productListName)
                .build(), "getAllProductLiters");
        return ResponseEntity.ok(response);
    }
}
