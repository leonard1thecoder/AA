package com.product.application.controllers;

import com.product.application.dto.CompanyProductRequest;
import com.product.application.services.CompanyProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company-products")
@RequiredArgsConstructor
public class CompanyProductsController {

    private final CompanyProductsService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CompanyProductRequest request) {
        return ResponseEntity.ok(service.createProduct(request));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> list(@PathVariable Long companyId) {
        return ResponseEntity.ok(service.getProductsByCompany(companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CompanyProductRequest request) {
        return ResponseEntity.ok(service.updateProduct(id, request));
    }

}
