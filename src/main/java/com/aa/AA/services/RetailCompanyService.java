package com.aa.AA.services;

import com.aa.AA.dtos.*;
import com.aa.AA.entities.RetailCompanyEntity;
import com.aa.AA.utils.exceptions.RetailCompanyAlreadyExistException;
import com.aa.AA.utils.exceptions.RetailCompanyNotFoundException;
import com.aa.AA.utils.executors.Execute;
import com.aa.AA.utils.mappers.RetailCompanyMapper;
import com.aa.AA.utils.repository.RetailCompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.concurrent.TimeUnit;

import static com.aa.AA.utils.exceptions.exceptionHandler.ExceptionHandler.throwExceptionAndReport;

@Service
public class RetailCompanyService implements Execute<List<RetailCompanyResponse>> {
    private Logger logger = LoggerFactory.getLogger(RetailCompanyService.class);

    private RetailCompanyRepository retailCompanyRepository;
    public static String serviceHandler;
    private RedisService redisService;
    private RegisterRetailCompanyRequest registerRetailCompanyRequest;
    private RetailCompanyMapper retailCompanyMapper;
    private PasswordEncoder passwordEncoder;
    private DisplayRetailCompanyByRetailCoRegNoRequest displayRetailCompanyByRetailCoRegNoRequest;
    private DisplayRetailCompanyByNameRequest displayRetailCompanyByNameRequest;
    private DisplayRetailCompaniesByOwnerNameRequest displayRetailCompaniesByOwnerNameRequest;

    public RetailCompanyService(@Autowired RetailCompanyMapper retailCompanyMapper, @Autowired RetailCompanyRepository retailCompanyRepository) {
        this.retailCompanyRepository = retailCompanyRepository;
        this.retailCompanyMapper = retailCompanyMapper;
    }

    public void setDisplayRetailCompanyByRetailCoRegNoRequest(DisplayRetailCompanyByRetailCoRegNoRequest displayRetailCompanyByRetailCoRegNoRequest) {
        this.displayRetailCompanyByRetailCoRegNoRequest = displayRetailCompanyByRetailCoRegNoRequest;
    }

    public void setDisplayRetailCompanyByNameRequest(DisplayRetailCompanyByNameRequest displayRetailCompanyByNameRequest) {
        this.displayRetailCompanyByNameRequest = displayRetailCompanyByNameRequest;
    }

    public void setDisplayRetailCompaniesByOwnerNameRequest(DisplayRetailCompaniesByOwnerNameRequest displayRetailCompaniesByOwnerNameRequest) {
        this.displayRetailCompaniesByOwnerNameRequest = displayRetailCompaniesByOwnerNameRequest;
    }

    public void setRegisterLiquorStoreRequest(RegisterRetailCompanyRequest registerRetailCompanyRequest) {
        this.registerRetailCompanyRequest = registerRetailCompanyRequest;
    }

    private List<RetailCompanyResponse> registerRetailCompany() {
        List<RetailCompanyResponse> retailCompanyResponseList;
        var liquorStoreEntity = new RetailCompanyEntity(null, registerRetailCompanyRequest.getUsersEntity(), registerRetailCompanyRequest.getPrivilegeEntity(), registerRetailCompanyRequest.getLiquorStoreName(), registerRetailCompanyRequest.getCountryName(), registerRetailCompanyRequest.getCityName(), registerRetailCompanyRequest.getLiquorStoreCertNo(), registerRetailCompanyRequest.getLiquorStoreStatus());
        if (retailCompanyRepository.findByLiquorStoreCertNo(registerRetailCompanyRequest.getLiquorStoreCertNo()).isPresent() || retailCompanyRepository.findByLiquorStoreName(registerRetailCompanyRequest.getLiquorStoreName()).isPresent()) {
            var errorMessage = "Retail company being registered already exists in our system";
            var resolveIssue = "please confirm name of retail company and registration no";
            throw throwExceptionAndReport(new RetailCompanyAlreadyExistException(errorMessage), errorMessage, resolveIssue);
        } else {
            retailCompanyResponseList = List.of(retailCompanyRepository.save(liquorStoreEntity))
                    .stream()
                    .peek((s) -> logger.info("registering retail company : {} was successful data : {}", registerRetailCompanyRequest.getLiquorStoreName(), s))
                    .map(retailCompanyMapper::toDto)
                    .toList();
        }
        return retailCompanyResponseList;
    }

    private List<RetailCompanyResponse> displayAllRetailCompanies() {
        return retailCompanyRepository.findAll()
                .stream()
                .map(retailCompanyMapper::toDto)
                .toList();
    }

    private List<RetailCompanyResponse> displayRetailCompanyByName() {
        var encrypt = passwordEncoder.encode(displayRetailCompanyByNameRequest.getRetailCompanyName());
        var redisRetailCompanyResponse = redisService.get(encrypt, RetailCompanyResponse.class);
        if (redisRetailCompanyResponse != null) {
            return List.of(redisRetailCompanyResponse);
        } else {
            var retailCompanyOptionalEntity = retailCompanyRepository.findByLiquorStoreName(displayRetailCompanyByNameRequest.getRetailCompanyName());
            if (retailCompanyOptionalEntity.isPresent()) {
                var jpaRetailCompanyResponse = retailCompanyOptionalEntity.stream()
                        .map(retailCompanyMapper::toDto)
                        .toList();
                logger.info("retail company : {} was successful found data : {}", displayRetailCompanyByNameRequest.getRetailCompanyName(), jpaRetailCompanyResponse);
                redisService.set(encrypt, jpaRetailCompanyResponse, 5L, TimeUnit.HOURS);
                return jpaRetailCompanyResponse;
            } else {
                var errorMessage = "Retail company not found when searching by name, retail company name : "+ displayRetailCompanyByNameRequest.getRetailCompanyName();
                var resolveIssue = "Please ensure that the company name provided is correct ";
                throw throwExceptionAndReport(new RetailCompanyNotFoundException(errorMessage),errorMessage,resolveIssue);
            }
        }
    }

    private List<RetailCompanyResponse> displayRetailCompanyByRetailCoRegNo() {
        var encrypt = passwordEncoder.encode(displayRetailCompanyByRetailCoRegNoRequest.getRetailCompanyRetailRegNo());
        var redisRetailCompanyResponse = redisService.get(encrypt, RetailCompanyResponse.class);
        if (redisRetailCompanyResponse != null) {
            return List.of(redisRetailCompanyResponse);
        } else {
            var retailCompanyOptionalEntity = retailCompanyRepository.findByLiquorStoreCertNo(displayRetailCompanyByRetailCoRegNoRequest.getRetailCompanyRetailRegNo());
            if (retailCompanyOptionalEntity.isPresent()) {
                var jpaRetailCompanyResponse = retailCompanyOptionalEntity.stream()
                        .map(retailCompanyMapper::toDto)
                        .toList();
                redisService.set(encrypt, jpaRetailCompanyResponse, 5L, TimeUnit.HOURS);
                logger.info("retail company no : {} was successful found data : {}", displayRetailCompanyByRetailCoRegNoRequest.getRetailCompanyRetailRegNo(), jpaRetailCompanyResponse);

                return jpaRetailCompanyResponse;
            } else {
                var errorMessage = "Retail company not found when searching by registration no, registration no: " +displayRetailCompanyByRetailCoRegNoRequest.getRetailCompanyRetailRegNo();
                var resolveIssue = "Please ensure that retail company registration number is correct";
                throw throwExceptionAndReport(new RetailCompanyNotFoundException(errorMessage),errorMessage,resolveIssue);
            }
        }
    }

    private List<RetailCompanyResponse> displayRetailCompaniesByOwnerName() {
        var encrypt = passwordEncoder.encode(displayRetailCompaniesByOwnerNameRequest.getUsersEntity().getUsersIdentityNo());
        var redisRetailCompanyResponse = redisService.get(encrypt, RetailCompanyResponse.class);
        if (redisRetailCompanyResponse != null) {
            return List.of(redisRetailCompanyResponse);
        } else {
            var retailCompanyOptionalEntity = retailCompanyRepository.findByOwnerName(displayRetailCompaniesByOwnerNameRequest.getUsersEntity());
            if (retailCompanyOptionalEntity.isPresent()) {
                var jpaRetailCompanyResponse = retailCompanyOptionalEntity.stream()
                        .map(retailCompanyMapper::toDto)
                        .toList();
                redisService.set(encrypt, jpaRetailCompanyResponse, 5L, TimeUnit.HOURS);
                logger.info("retail company owner : {} was successful found data : {}", displayRetailCompaniesByOwnerNameRequest.getUsersEntity().getUsersIdentityNo(), jpaRetailCompanyResponse);

                return jpaRetailCompanyResponse;
            } else {
                var errorMessage =  "Retail company not found when searching by owner, owner name : " + displayRetailCompaniesByOwnerNameRequest.getUsersEntity().getUsersIdentityNo() ;
                var resolveIssue = "Please ensure that the identity number is correct";
                throw throwExceptionAndReport(new RetailCompanyNotFoundException(errorMessage),errorMessage,resolveIssue);
            }
        }
    }

    @Override
    public List<RetailCompanyResponse> call() throws Exception {
        switch (serviceHandler) {
            case "registerRetailCompany":
                return registerRetailCompany();
            case "displayRetailCompaniesByOwnerName":
                return displayRetailCompaniesByOwnerName();
            case "displayRetailCompanyByRetailCoRegNo":
                return displayRetailCompanyByRetailCoRegNo();
            case "displayRetailCompanyByName":
                return displayRetailCompanyByName();
            case "displayAllRetailCompanies":
                return displayAllRetailCompanies();
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public void setCache(@Autowired RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void setEncodeCacheKey(@Autowired PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
