package com.aa.AA.services;

import com.aa.AA.dtos.*;
import com.aa.AA.entities.RetailCompanyEntity;
import com.aa.AA.utils.exceptions.RetailCompanyAlreadyExistException;
import com.aa.AA.utils.exceptions.RetailCompanyNotFoundException;
import com.aa.AA.utils.executors.Execute;
import com.aa.AA.utils.mappers.RetailCompanyMapper;
import com.aa.AA.utils.repository.RetailCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Service
public class RetailCompanyService implements Execute<List<RetailCompanyResponse>> {

    private RetailCompanyRepository retailCompanyRepository;
    private static String serviceHandler;
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
        var liquorStoreEntity = new RetailCompanyEntity(null,registerRetailCompanyRequest.getUsersEntity(), registerRetailCompanyRequest.getPrivilegeEntity(), registerRetailCompanyRequest.getLiquorStoreName(), registerRetailCompanyRequest.getCountryName(), registerRetailCompanyRequest.getCityName(), registerRetailCompanyRequest.getLiquorStoreCertNo(), registerRetailCompanyRequest.getLiquorStoreStatus());
        if (retailCompanyRepository.findByLiquorStoreCertNo(registerRetailCompanyRequest.getLiquorStoreCertNo()).isPresent() || retailCompanyRepository.findByLiquorStoreName(registerRetailCompanyRequest.getLiquorStoreName()).isPresent()) {
            throw new RetailCompanyAlreadyExistException("Retail company you trying to already exists, confirm your license or name");
        } else {
            retailCompanyResponseList = List.of(retailCompanyRepository.save(liquorStoreEntity))
                    .stream()
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
                redisService.set(encrypt, jpaRetailCompanyResponse, 5L, TimeUnit.HOURS);
                return jpaRetailCompanyResponse;
            } else
                throw new RetailCompanyNotFoundException("Retail company not found when searching by name");
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
                return jpaRetailCompanyResponse;
            } else
                throw new RetailCompanyNotFoundException("Retail company not found when searching by registration no");
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
                redisService.set(encrypt,jpaRetailCompanyResponse,5L, TimeUnit.HOURS);
                return jpaRetailCompanyResponse;
            }else
                throw new RetailCompanyNotFoundException("Retail company not found when searching by owner");
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
