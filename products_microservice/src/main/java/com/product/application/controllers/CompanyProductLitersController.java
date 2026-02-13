package com.product.application.controllers;


import com.product.application.dto.GetProductLiterByNameRequest;
import com.product.application.dto.ProductLitersResponse;
import com.product.application.dto.RegisterProductLitersRequest;
import com.product.application.executor.ProductServiceContractExecutor;
import com.product.application.services.ProductsLitersService;
import com.utils.application.ResponseContract;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/dev/api/product/liter")
@RequestMapping
public class CompanyProductLitersController {

    private final ProductsLitersService productsLitersService;
    private final ProductServiceContractExecutor serviceExecutor;

    public CompanyProductLitersController(ProductsLitersService productsLitersService) {
        this.productsLitersService = productsLitersService;
        this.serviceExecutor = ProductServiceContractExecutor.getInstance();
    }

    @GetMapping("getAllProductLiter")
    private ResponseEntity<List<? extends ResponseContract>> getAllProducts() {
        var response = serviceExecutor.buildContactFormServiceExecutor(productsLitersService, null, "getAllProductLiters");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addProductLiter")
    private ResponseEntity<List<? extends ResponseContract>> addProducts(RegisterProductLitersRequest request) {
        var response = serviceExecutor.buildContactFormServiceExecutor(productsLitersService, request, "getAllProductLiters");
        if (response.getFirst() instanceof ProductLitersResponse) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/getProductLiterByName/{productLiterName}")
    private ResponseEntity<List<? extends ResponseContract>> getProductByName(@PathVariable String productLiterName) {
        var response = serviceExecutor.buildContactFormServiceExecutor(productsLitersService, GetProductLiterByNameRequest
                .builder()
                .productLiters(productLiterName)
                .build(), "getAllProductLiters");

        if (response.getFirst() instanceof ProductLitersResponse) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
