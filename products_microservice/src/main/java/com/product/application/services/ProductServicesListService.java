package com.product.application.services;


import com.product.application.dto.GetProductByNameRequest;
import com.product.application.dto.ProductListResponse;
import com.product.application.dto.RegisterProductListRequest;
import com.product.application.entities.ProductList;
import com.product.application.exceptions.NotPrivilegedToSentRequestException;
import com.product.application.repositories.ProductListRepository;
import com.users.application.exceptions.UserNotFoundException;
import com.users.application.repository.UsersRepository;
import com.utils.application.RedisService;
import com.utils.application.RequestContract;
import com.utils.application.ResponseContract;
import com.utils.application.globalExceptions.IncorrectRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

import static com.utils.application.ExceptionHandler.throwExceptionAndReport;

@Service
public class ProductServicesListService implements ProductServicesContract {
private Logger logger = LoggerFactory.getLogger(ProductServicesListService.class);


    private final ProductListRepository productListRepository;
    private final UsersRepository usersRepository;
    private final RedisService redisService;


    public ProductServicesListService(ProductListRepository productListRepository, UsersRepository usersRepository, RedisService redisService) {
        this.productListRepository = productListRepository;
        this.usersRepository = usersRepository;
        this.redisService = redisService;
    }

    private void addProduct(RequestContract request) {

        if (request instanceof RegisterProductListRequest castedRequest) {
            var optionalAdministrator = usersRepository.findByUserFullName(castedRequest.getAdministrator().getFullName());

            if (optionalAdministrator.isPresent()) {
                if (optionalAdministrator.get().getPrivileges().getPrivilegeName().equals("Administrator")) {
                    productListRepository.save(ProductList
                            .builder()
                            .productName(castedRequest.getProductName())
                            .productStatus((byte) 1)
                            .productImagePath(castedRequest.getProductImagePath())

                            .administrator(optionalAdministrator.get())
                            .build());
                }else{
                    var errorMessage = "User submitting request to add product,  is not privileged";
                    var resolveIssue = "Ensure you administrator before sending request to add product list";
                    throw throwExceptionAndReport(new NotPrivilegedToSentRequestException(errorMessage), errorMessage, resolveIssue);
                }
            }else{
                var errorMessage = "User submitting request to add product not found";
                var resolveIssue = "ensure correct use to add product";
                throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, resolveIssue);
            }
        }else{
            var errorMessage = "Request sent is not correct to submit add product request";
            var resolveIssue = "Use correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);

        }
    }

    private List<ProductListResponse> getAllAddedProducts(){
        return mapToResponse(productListRepository.findAll());
    }

    private List<ProductListResponse> getProductByName(RequestContract request){
        if(request instanceof GetProductByNameRequest castedRequest){
           var optionalProduct= productListRepository.findByProductName(castedRequest.getProductName());
            if(optionalProduct.isPresent()){
                return mapToResponse(optionalProduct.get());
            }else{
                var errorMessage = "Request sent is not correct to submit add product request";
                var resolveIssue = "Use correct request";
                throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);
            }
        }else{
            var errorMessage = "Request sent is not correct to submit add product request";
            var resolveIssue = "Use correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);
        }
    }

    private List<ProductListResponse> mapToResponse(List<ProductList> productLists){
        return productLists
                .parallelStream()
                .map(s-> ProductListResponse
                        .builder()
                        .productId(s.getProductId())
                        .productName(s.getProductName())
                        .productImagePath(s.getProductImagePath())
                        .productStatus(s.getProductStatus())
                        .build())
                .toList();
    }

    private List<ProductListResponse> mapToResponse(ProductList productList){
        return Stream
                .of(productList)
                .map(s-> ProductListResponse
                        .builder()
                        .productId(s.getProductId())
                        .productName(s.getProductName())
                        .productImagePath(s.getProductImagePath())
                        .productStatus(s.getProductStatus())
                        .build())
                .toList();
    }

    @Override
    public List<? extends ResponseContract> call(String serviceRunner, RequestContract request) {
        switch(serviceRunner) {

        }
        return List.of();
    }
}
