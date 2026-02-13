package com.product.application.services;

import com.product.application.dto.*;
import com.product.application.entities.CompanyProducts;
import com.product.application.entities.ProductList;
import com.product.application.entities.ProductsLiters;
import com.product.application.exceptions.CompanyProductListNotFoundException;
import com.product.application.exceptions.ProductLiterNotFoundException;
import com.product.application.repositories.CompanyProductsRepository;
import com.product.application.repositories.ProductListRepository;
import com.product.application.repositories.ProductLitersRepository;
import com.retails.application.entity.RetailCompany;
import com.retails.application.exceptions.RetailCompanyNotFoundException;
import com.retails.application.repository.RetailCompanyRepository;
import com.utils.application.RequestContract;
import com.utils.application.ResponseContract;
import com.utils.application.globalExceptions.IncorrectRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static com.utils.application.CommonMethods.formatDateTime;
import static com.utils.application.ExceptionHandler.throwExceptionAndReport;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CompanyProductsService implements ProductServicesContract {

    private final CompanyProductsRepository companyProductsRepo;
    private final RetailCompanyRepository retailCompanyRepo;
    private final ProductListRepository productListRepo;
    private final ProductLitersRepository productsLitersRepo;

    private List<CompanyProductResponse> createProduct(RequestContract request) {

        if (request instanceof CompanyProductRequest castedRequest) {
            log.info("Creating company product for companyId={}, productListId={}",
                    castedRequest.getCompanyId(), castedRequest.getProductListId());

            RetailCompany company = retailCompanyRepo.findById(castedRequest.getCompanyId())
                    .orElseThrow(() -> {   var errorMessage = "Retail company  not found";
                        var resolveIssue = "Please check your retail company id";
                        return throwExceptionAndReport(new RetailCompanyNotFoundException(errorMessage), errorMessage, resolveIssue);
                    });

            ProductList productList = productListRepo.findById(castedRequest.getProductListId())
                    .orElseThrow(() ->{    var errorMessage = "Product list not found";
                        var resolveIssue = "Please check product list id";
                        return throwExceptionAndReport(new CompanyProductListNotFoundException(errorMessage), errorMessage, resolveIssue);

                    });

            ProductsLiters liters = productsLitersRepo.findById(castedRequest.getProductsLitersId())
                    .orElseThrow(() ->{    var errorMessage = "Product liter not found";
                        var resolveIssue = "Please  check product liter id";
                        return throwExceptionAndReport(new ProductLiterNotFoundException(errorMessage), errorMessage, resolveIssue);
                    });

            CompanyProducts entity = CompanyProducts.builder()
                    .companyProduct_owner(company)
                    .productList(productList)
                    .productsLiters(liters)
                    .productQuantity(castedRequest.getProductQuantity())
                    .productPrice(castedRequest.getProductPrice())
                    .returnableBottlePrice(castedRequest.getReturnableBottlePrice())
                    .lateNightPriceIncrease(castedRequest.getLateNightPriceIncrease())
                    .companyProductStatus(castedRequest.getCompanyProductStatus())
                    .lateNightPriceStatus(castedRequest.getLateNightPriceStatus())
                    .registrationDate(formatDateTime(LocalDateTime.now()))
                    .modifiedDate(formatDateTime(LocalDateTime.now()))
                    .build();

            CompanyProducts saved = companyProductsRepo.save(entity);

            log.info("Company product created successfully id={}", saved.getCompanyProductId());

            return mapToResponse(saved);
        } else {
            var errorMessage = "Request sent is not correct to submit add product request";
            var resolveIssue = "Use correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);

        }
    }


    @Transactional(readOnly = true)
    protected List<CompanyProductResponse> getProductsByCompany(RequestContract request) {

        if (request instanceof GetProductByCompanyId castedRequest) {
            log.info("Fetching products for companyId={}", castedRequest.getCompanyId());

            return companyProductsRepo.findByCompanyProduct_owner_Id(castedRequest.getCompanyId())
                    .stream()
                    .map(s->CompanyProductResponse.builder()
                            .companyProductId(s.getCompanyProductId())
                            .companyName(s.getCompanyProduct_owner().getRetailCompanyName())
                            .productName(s.getProductList().getProductName())
                            .productQuantity(s.getProductQuantity())
                            .productPrice(s.getProductPrice())
                            .status(s.getCompanyProductStatus())
                            .registrationDate(s.getRegistrationDate())
                            .modifiedDate(s.getModifiedDate())
                            .build())
                    .toList();
        } else {
            var errorMessage = "Request sent is not correct to submit add product request";
            var resolveIssue = "Use correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);

        }
    }


    @Transactional(readOnly = true)
    protected List<CompanyProductResponse> getProductById(RequestContract request) {
        if (request instanceof GetProductByCompanyProductId castedRequest) {
            log.info("Fetching company product id={}", castedRequest.getCompanyProductId());

            CompanyProducts product = companyProductsRepo.findById(castedRequest.getCompanyProductId())
                    .orElseThrow(() ->{   var errorMessage = "Request sent is not correct to submit add product request";
                        var resolveIssue = "Use correct request";
                        return throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);
                    });

            return mapToResponse(product);
        } else {
            var errorMessage = "Request sent is not correct to submit add product request";
            var resolveIssue = "Use correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);

        }
    }


    private List<CompanyProductResponse> updateQuantity(RequestContract request) {

        if (request instanceof UpdateCompanyProductQuantityRequest castedRequest) {
            log.info("Updating quantity productId={}, newQuantity={}", castedRequest.getCompanyProductId(), castedRequest.getQuantity());

            CompanyProducts product = companyProductsRepo.findById(castedRequest.getCompanyProductId())
                    .orElseThrow(() -> {   var errorMessage = "Request sent is not correct to submit add product request";
                        var resolveIssue = "Use correct request";
                        return throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);
                    });

            product.setProductQuantity(castedRequest.getQuantity());
            product.setModifiedDate(formatDateTime(LocalDateTime.now()));

           return mapToResponse( companyProductsRepo.save(product));
        }else{
            var errorMessage = "Request sent is not correct to submit add product request";
            var resolveIssue = "Use correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);
        }
    }

    private List<CompanyProductResponse> mapToResponse(CompanyProducts entity) {
        return Stream.of(entity)
                .map(s->CompanyProductResponse.builder()
                .companyProductId(s.getCompanyProductId())
                .companyName(s.getCompanyProduct_owner().getRetailCompanyName())
                .productName(s.getProductList().getProductName())
                .productQuantity(s.getProductQuantity())
                .productPrice(s.getProductPrice())
                .status(s.getCompanyProductStatus())
                .registrationDate(s.getRegistrationDate())
                .modifiedDate(s.getModifiedDate())
                .build())
                .toList();
    }

    @Override
    public List<? extends ResponseContract> call(String serviceRunner, RequestContract request) {
        return switch (serviceRunner) {
            case "createProduct" -> this.createProduct(request);
            case "getProductById" -> this.getProductById(request);
            case "updateQuantity" -> this.updateQuantity(request);
            case "getProductsByCompany" -> this.getProductsByCompany(request);
            default -> throw new IllegalStateException("Unexpected value: " + serviceRunner);
        };
    }
}
