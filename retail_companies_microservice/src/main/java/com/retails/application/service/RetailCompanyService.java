package com.retails.application.service;


import com.retails.application.dto.*;
import com.retails.application.entity.RetailCompany;
import com.retails.application.repository.RetailCompanyRepository;
import com.utils.application.RedisService;
import lombok.Setter;
import org.springframework.stereotype.Service;
import com.retails.application.exceptions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.utils.application.ExceptionHandler.throwExceptionAndReport;

@Service
public class RetailCompanyService implements Callable<List<RetailCompanyResponse>> {
    private final RetailCompanyRepository retailCompanyRepository;
    public static String serviceHandler;
    private final RedisService redisService;
    @Setter
    private RegisterRetailCompanyRequest registerRetailCompanyRequest;
    @Setter
    private DisplayRetailCompanyByIdRequest displayRetailCompanyByIdRequest;
    @Setter
    private DisplayRetailCompanyByNameRequest displayRetailCompanyByNameRequest;
    @Setter
    private DisplayRetailCompaniesByOwnerNameRequest displayRetailCompaniesByOwnerNameRequest;

    public RetailCompanyService(RetailCompanyRepository retailCompanyRepository, RedisService redisService) {
        this.retailCompanyRepository = retailCompanyRepository;
        this.redisService = redisService;
    }

    private List<RetailCompanyResponse> registerRetailCompany() {
        var optRetailCompany = retailCompanyRepository.findByRetailCompanyName(registerRetailCompanyRequest.getRetailCompanyName());
        if (optRetailCompany.isPresent()) {
            var errorMessage = " Retail Company already";
            var resolveIssue = "Change the name of retail company";
            throw throwExceptionAndReport(new RetailCompanyAlreadyExistException(errorMessage), errorMessage, resolveIssue);
        } else {

            var retailCompany = retailCompanyRepository.save(RetailCompany
                    .builder()
                    .fkUsersId(registerRetailCompanyRequest.getFk_user_id())
                    .registrationDate(formatDateTime(LocalDateTime.now()))
                    .modifiedDate(formatDateTime(LocalDateTime.now()))
                    .fkPrivilegeId(registerRetailCompanyRequest.getFk_privilege_id())
                    .retailCompanyName(registerRetailCompanyRequest.getRetailCompanyName())
                    .countryName(registerRetailCompanyRequest.getCountryName()).
                    cityName(registerRetailCompanyRequest.getCityName())
                    .retailCompanyCertNo(registerRetailCompanyRequest.getRetailCompanyCertNo())
                    .retailCompanyStatus(registerRetailCompanyRequest.getRetailCompanyStatus())
                    .build());

            return mapToResponse(List.of(retailCompany));
        }
    }

    private List<RetailCompanyResponse> mapToResponse(List<RetailCompany> list) {
        return list
                .parallelStream()
                .map(s ->
                        RetailCompanyResponse
                                .builder()
                                .id(s.getId())
                                .retailCompanyName(s.getRetailCompanyName())
                                .countryName(s.getCountryName())
                                .cityName(s.getCityName())
                                .retailCompanyCertNo(s.getRetailCompanyCertNo())
                                .registrationDate(s.getRegistrationDate())
                                .modifiedDate(s.getModifiedDate())
                                .retailCompanyStatus(s.getRetailCompanyStatus())
                                .build())
                .toList();
    }

    private String formatDateTime(LocalDateTime issueDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return issueDate.format(formatter);
    }

    private List<RetailCompanyResponse> displayAllRetailCompanies() {
        return mapToResponse(retailCompanyRepository.findAll());
    }

    private List<RetailCompanyResponse> displayRetailCompanyByName() {
        var encrypt = displayRetailCompanyByNameRequest.getRetailCompanyName();
        var redisRetailCompanyResponse = redisService.get(encrypt, RetailCompanyResponse.class);
        if (redisRetailCompanyResponse != null) {
            return List.of(redisRetailCompanyResponse);
        } else {
            var retailCompanyOptionalEntity = retailCompanyRepository.findByRetailCompanyName(displayRetailCompanyByNameRequest.getRetailCompanyName());
            if (retailCompanyOptionalEntity.isPresent()) {
                var jpaRetailCompanyResponse = mapToResponse(retailCompanyOptionalEntity
                        .stream()
                        .toList());
                redisService.set(encrypt, jpaRetailCompanyResponse.get(0), 5L, TimeUnit.HOURS);
                return jpaRetailCompanyResponse;
            } else
                throw new RetailCompanyNotFoundException("Retail company not found when searching by name");
        }
    }

    @Override
    public List<RetailCompanyResponse> call() throws Exception {
        return switch (serviceHandler) {
            case "registerRetailCompany" -> registerRetailCompany();
            case "displayRetailCompanyByName" -> displayRetailCompanyByName();
            case "displayAllRetailCompanies" -> displayAllRetailCompanies();
            case "displayRetailCompanyById" -> displayRetailCompanyById();

            default -> throw new RuntimeException();
        };
    }

    private List<RetailCompanyResponse> displayRetailCompanyById() {

        var redisRetailCompanyResponse = redisService.get(displayRetailCompanyByIdRequest.getRetailCompanyId().toString(), RetailCompanyResponse.class);
        if(redisRetailCompanyResponse != null)
            return List.of(redisRetailCompanyResponse);
        else {
            var optRetailCompany = this.retailCompanyRepository.findById(displayRetailCompanyByIdRequest.getRetailCompanyId());

            if (optRetailCompany.isPresent()) {
                var  list = optRetailCompany
                        .stream()
                        .toList();
                var jpaRetailCompanyResponse = mapToResponse(list);
                redisService.set(displayRetailCompanyByIdRequest.getRetailCompanyId().toString(), jpaRetailCompanyResponse.get(0), 5L, TimeUnit.HOURS);

                return jpaRetailCompanyResponse;
            } else {
                var errorMessage = " Retail Company with id : " + displayRetailCompanyByIdRequest.getRetailCompanyId();
                var resolveIssue = "Check out company id";
                throw throwExceptionAndReport(new RetailCompanyNotFoundException(errorMessage), errorMessage, resolveIssue);
            }
        }
    }

}
