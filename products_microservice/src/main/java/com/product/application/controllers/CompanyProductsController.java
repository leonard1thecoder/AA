package com.product.application.controllers;

import com.product.application.dto.*;
import com.product.application.executor.ProductServiceContractExecutor;
import com.product.application.services.CompanyProductsService;
import com.utils.application.ResponseContract;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company-products")
public class CompanyProductsController {

    private final CompanyProductsService service;
    private final ProductServiceContractExecutor serviceExecutor;

    public CompanyProductsController(CompanyProductsService service) {
        this.service = service;
        this.serviceExecutor = ProductServiceContractExecutor.getInstance();
    }

    @PostMapping
    public ResponseEntity<List<? extends ResponseContract>> create(@RequestBody CompanyProductRequest request) {

        var response = this.serviceExecutor.buildContactFormServiceExecutor(service, request, "createProduct");
        if (response.getFirst() instanceof CompanyProductResponse)
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/products/{product_id}")
    public ResponseEntity<?> getProductByProductId(@PathVariable Long product_id) {
        var response = this.serviceExecutor.buildContactFormServiceExecutor(service, GetProductByCompanyProductId
                .builder()
                .companyProductId(product_id)
                .build(),
                "getProductById");

        if (response.getFirst() instanceof CompanyProductResponse)
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> list(@PathVariable Long companyId) {
        var response = this.serviceExecutor.buildContactFormServiceExecutor(service, GetProductByCompanyId
                        .builder()
                        .companyId(companyId)
                        .build(),
                "getProductsByCompany");

        if (response.getFirst() instanceof CompanyProductResponse)
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuantity(@PathVariable Long id, @RequestBody UpdateCompanyProductQuantityRequest request) {
        request.setCompanyProductId(id);
        var response = this.serviceExecutor.buildContactFormServiceExecutor(service, request, "updateQuantity");
        if (response.getFirst() instanceof CompanyProductResponse)
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.badRequest().body(response);
    }

}
