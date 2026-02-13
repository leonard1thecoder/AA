package com.product.application.services;

import com.product.application.dto.GetProductLiterByNameRequest;
import com.product.application.dto.ProductLitersResponse;
import com.product.application.dto.RegisterProductLitersRequest;
import com.product.application.entities.ProductsLiters;
import com.product.application.exceptions.NotPrivilegedToSentRequestException;
import com.product.application.exceptions.ProductLiterNotFoundException;
import com.product.application.repositories.ProductLitersRepository;
import com.users.application.exceptions.UserNotFoundException;
import com.users.application.repository.UsersRepository;
import com.utils.application.RedisService;
import com.utils.application.RequestContract;
import com.utils.application.ResponseContract;
import com.utils.application.globalExceptions.IncorrectRequestException;

import java.util.List;
import java.util.stream.Stream;

import static com.utils.application.ExceptionHandler.throwExceptionAndReport;

public class ProductsLitersService implements ProductServicesContract {

    private final UsersRepository usersRepository;
    private final RedisService redisService;
    private final ProductLitersRepository productLitersRepository;

    public ProductsLitersService(UsersRepository usersRepository, RedisService redisService, ProductLitersRepository productLitersRepository) {
        this.usersRepository = usersRepository;
        this.redisService = redisService;
        this.productLitersRepository = productLitersRepository;
    }

    private List<ProductLitersResponse> addProduct(RequestContract request) {

        if (request instanceof RegisterProductLitersRequest castedRequest) {
            var optionalAdministrator = usersRepository.findByUserFullName(castedRequest.getAdministrator().getUserFullName());

            if (optionalAdministrator.isPresent()) {
                if (optionalAdministrator.get().getPrivileges().getPrivilegeName().equals("Administrator")) {

                    return   mapToResponse(productLitersRepository.save(ProductsLiters
                            .builder()
                            .productLiters(castedRequest.getLiters())
                            .status((byte) 1)
                            .administrator(optionalAdministrator.get())
                            .bottleReturnableStatus(castedRequest.getBottleReturnableStatus())
                            .build()));
                } else {
                    var errorMessage = "User submitting request to add product,  is not privileged";
                    var resolveIssue = "Ensure you administrator before sending request to add product list";
                    throw throwExceptionAndReport(new NotPrivilegedToSentRequestException(errorMessage), errorMessage, resolveIssue);
                }
            } else {
                var errorMessage = "User submitting request to add product liter not found";
                var resolveIssue = "ensure correct use to add product";
                throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, resolveIssue);
            }
        } else {
            var errorMessage = "Request sent is not correct to submit add product request";
            var resolveIssue = "Use correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);

        }
    }

    private List<ProductLitersResponse> getAllProductLiters() {
        return mapToResponse(this.productLitersRepository.findAll());
    }

    private List<ProductLitersResponse> getProductLiterByLitersName(RequestContract request) {
        if (request instanceof GetProductLiterByNameRequest castedRequest) {
            var optionalProductLiter = this.productLitersRepository.findByProductLiters(castedRequest.getProductLiters());
            if (optionalProductLiter.isPresent()) {
                var productLiter = optionalProductLiter.get();
                return mapToResponse(productLiter);
            } else {
                var errorMessage = "Product Liter not found by name";
                var resolveIssue = "Please provide the correct product Lite name";
                throw throwExceptionAndReport(new ProductLiterNotFoundException(errorMessage), errorMessage, resolveIssue);
            }

        } else {
            var errorMessage = "Request sent is not correct to submit add product request";
            var resolveIssue = "Use correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);

        }
    }

    private List<ProductLitersResponse> mapToResponse(ProductsLiters entity) {
        return Stream
                .of(entity)
                .map(s -> ProductLitersResponse
                        .builder()
                        .productLiters(s.getProductLiters())
                        .registeredDate(s.getRegisteredDate())
                        .status(s.getStatus())
                        .administratorName(s.getAdministrator().getUserFullName())
                        .build())
                .toList();
    }

    private List<ProductLitersResponse> mapToResponse(List<ProductsLiters> entity) {
        return entity.parallelStream()
                .map(s -> ProductLitersResponse
                        .builder()
                        .productLiters(s.getProductLiters())
                        .registeredDate(s.getRegisteredDate())
                        .status(s.getStatus())
                        .administratorName(s.getAdministrator().getUserFullName())
                        .build())
                .toList();
    }

    @Override
    public List<? extends ResponseContract> call(String serviceRunner, RequestContract request) {
        return switch (serviceRunner) {
            case "getAllProductLiters" -> this.getAllProductLiters();
            case "addProduct" -> this.addProduct(request);
            case "getProductLiterByLitersName" -> this.getProductLiterByLitersName(request);
            default -> throw new IllegalStateException("Unexpected value: " + serviceRunner);
        };
    }
}
