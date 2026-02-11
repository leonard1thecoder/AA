package com.product.application.services;

import com.product.application.dto.CompanyProductRequest;
import com.product.application.dto.CompanyProductResponse;
import com.product.application.entities.CompanyProducts;

import com.product.application.entities.ProductList;
import com.product.application.entities.ProductsLiters;
import com.product.application.repositories.CompanyProductsRepository;
import com.product.application.repositories.ProductListRepository;
import com.product.application.repositories.ProductLitersRepository;
import com.product.application.services.ProductServicesContract;
import com.retails.application.entity.RetailCompany;
import com.retails.application.repository.RetailCompanyRepository;
import com.utils.application.RequestContract;
import com.utils.application.ResponseContract;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CompanyProductsService implements ProductServicesContract {

    private final CompanyProductsRepository companyProductsRepo;
    private final RetailCompanyRepository retailCompanyRepo;
    private final ProductListRepository productListRepo;
    private final ProductLitersRepository productsLitersRepo;

    public CompanyProductResponse createProduct(CompanyProductRequest request) {

        log.info("Creating company product for companyId={}, productListId={}",
                request.getCompanyId(), request.getProductListId());

        RetailCompany company = retailCompanyRepo.findById(request.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        ProductList productList = productListRepo.findById(request.getProductListId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        ProductsLiters liters = productsLitersRepo.findById(request.getProductsLitersId())
                .orElseThrow(() -> new EntityNotFoundException("Liters not found"));

        CompanyProducts entity = CompanyProducts.builder()
                .companyProduct_owner(company)
                .productList(productList)
                .productsLiters(liters)
                .productQuantity(request.getProductQuantity())
                .productPrice(request.getProductPrice())
                .returnableBottlePrice(request.getReturnableBottlePrice())
                .lateNightPriceIncrease(request.getLateNightPriceIncrease())
                .companyProductStatus(request.getCompanyProductStatus())
                .lateNightPriceStatus(request.getLateNightPriceStatus())
                .registrationDate(LocalDateTime.now().toString())
                .modifiedDate(LocalDateTime.now().toString())
                .build();

        CompanyProducts saved = companyProductsRepo.save(entity);

        log.info("Company product created successfully id={}", saved.getCompanyProductId());

        return mapToResponse(saved);
    }

    public CompanyProductResponse updateProduct(Long id, CompanyProductRequest request) {

        log.info("Updating company product id={}", id);

        CompanyProducts product = companyProductsRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company product not found"));

        product.setProductQuantity(request.getProductQuantity());
        product.setProductPrice(request.getProductPrice());
        product.setReturnableBottlePrice(request.getReturnableBottlePrice());
        product.setLateNightPriceIncrease(request.getLateNightPriceIncrease());
        product.setCompanyProductStatus(request.getCompanyProductStatus());
        product.setLateNightPriceStatus(request.getLateNightPriceStatus());
        product.setModifiedDate(LocalDateTime.now().toString());

        CompanyProducts updated = companyProductsRepo.save(product);

        log.info("Company product updated id={}", id);

        return mapToResponse(updated);
    }

    @Transactional(readOnly = true)
    public List<CompanyProductResponse> getProductsByCompany(Long companyId) {

        log.info("Fetching products for companyId={}", companyId);

        return companyProductsRepo.findByCompanyProduct_owner_Id(companyId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    
    @Transactional(readOnly = true)
    public CompanyProductResponse getProductById(Long id) {

        log.info("Fetching company product id={}", id);

        CompanyProducts product = companyProductsRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        return mapToResponse(product);
    }


    public void updateQuantity(Long id, Integer quantity) {

        log.info("Updating quantity productId={}, newQuantity={}", id, quantity);

        CompanyProducts product = companyProductsRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.setProductQuantity(quantity);
        product.setModifiedDate(LocalDateTime.now().toString());

        companyProductsRepo.save(product);
    }

    private CompanyProductResponse mapToResponse(CompanyProducts entity) {
        return CompanyProductResponse.builder()
                .companyProductId(entity.getCompanyProductId())
                .companyName(entity.getCompanyProduct_owner().getRetailCompanyName())
                .productName(entity.getProductList().getProductName())
                .productQuantity(entity.getProductQuantity())
                .productPrice(entity.getProductPrice())
                .status(entity.getCompanyProductStatus())
                .registrationDate(entity.getRegistrationDate())
                .modifiedDate(entity.getModifiedDate())
                .build();
    }

    @Override
    public List<? extends ResponseContract> call(String serviceRunner, RequestContract request) {
        return List.of();
    }
}
