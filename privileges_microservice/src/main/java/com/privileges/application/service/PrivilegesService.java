package com.privileges.application.service;

import com.privileges.application.dtos.AddPrivilegesRequest;
import com.privileges.application.dtos.FindByNameRequest;
import com.privileges.application.dtos.PrivilegesResponse;
import com.privileges.application.entity.Privileges;
import com.privileges.application.exceptions.PrivilegeAlreadyExistsException;
import com.privileges.application.exceptions.PrivilegeNotFoundException;
import com.privileges.application.repository.PrivilegesRepository;
import com.utils.application.RedisService;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.utils.application.ExceptionHandler.throwExceptionAndReport;


@Service
public class PrivilegesService {


    private final PrivilegesRepository privilegesRepository;

    private final RedisService redisService;

    @Setter
    private AddPrivilegesRequest addPrivilegesRequest;
    @Setter
    private FindByNameRequest findByPrivilegeNameRequest;

    public PrivilegesService(PrivilegesRepository privilegesRepository, RedisService redisService) {
        this.privilegesRepository = privilegesRepository;
        this.redisService = redisService;
    }

    public List<PrivilegesResponse> call(String privilegeServiceManager) throws Exception {

        return switch (privilegeServiceManager) {
            case "getAllPrivileges" -> this.getAllPrivileges();
            case "AddPrivileges" -> this.addPrivileges();
            case "getPrivilegeByName" -> this.findPrivilegeByName();

            default -> throw new NullPointerException("Privilege selected to execute not available");
        };
    }

    private List<PrivilegesResponse> findPrivilegeByName() {
        var optPrivilegeValue = this.privilegesRepository.findByPrivilegeName(findByPrivilegeNameRequest.getPrivilegeName());
        if (optPrivilegeValue.isPresent()) {
            return mapToResponse(optPrivilegeValue
                    .stream()
                    .toList());
        } else {
            var errorMessage = "privilege name" + findByPrivilegeNameRequest.getPrivilegeName() + "not found";
            var resolveIssue = "please enter the privilege name ";
            throw throwExceptionAndReport(new PrivilegeNotFoundException(errorMessage), errorMessage, resolveIssue);
        }
    }

    private List<PrivilegesResponse> addPrivileges() {
        var optPrivilegeValue = this.privilegesRepository.findByPrivilegeName(addPrivilegesRequest.getPrivilegeName());
        if (optPrivilegeValue.isEmpty()) {
            var privilege = Privileges
                    .builder()
                    .privilegeName(addPrivilegesRequest.getPrivilegeName())
                    .privilegeStatus((byte) 0)
                    .build();

            return mapToResponse(List.of(privilegesRepository.save(privilege)));
        }else {
            var errorMessage = "privilege name" + findByPrivilegeNameRequest.getPrivilegeName() + "exists";
            var resolveIssue = "please enter another name";
            throw throwExceptionAndReport(new PrivilegeAlreadyExistsException(errorMessage), errorMessage, resolveIssue);
        }
    }

    private List<PrivilegesResponse> mapToResponse(List<Privileges> list) {
        return list.parallelStream().map(s -> PrivilegesResponse
                .builder()
                .privilegeName(s.getPrivilegeName())
                .id(s.getId())
                .build()).toList();
    }

    private List<PrivilegesResponse> getAllPrivileges() {
        return mapToResponse(this.privilegesRepository.findAll());
    }
}
