package com.users.application.services;


import com.privileges.application.entity.Privileges;
import com.privileges.application.exceptions.PrivilegeNotFoundException;
import com.privileges.application.repository.PrivilegesRepository;
import com.users.application.exceptions.*;
import com.users.application.exceptions.controllerAdvice.UsersControllerAdvice;
import com.users.application.repository.UsersRepository;
import com.utils.application.JwtService;
import com.utils.application.RedisService;
import com.users.application.dtos.*;
import com.users.application.entities.Users;


import com.utils.application.RequestContract;
import com.utils.application.ServiceContract;
import com.utils.application.globalExceptions.IncorrectRequestException;
import com.utils.application.globalExceptions.ServiceHandlerException;
import com.utils.application.mailing.dto.VerifyCustomerEvent;
import com.utils.application.mailing.dto.VerifyUpdatePasswordEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


import static com.users.application.validators.UsersFieldsDataValidator.getInstance;
import static com.utils.application.ExceptionHandler.throwExceptionAndReport;


@Service
public class UsersService implements ServiceContract {
    private static final Logger logger = LoggerFactory.getLogger(UsersService.class);

    private final ApplicationEventPublisher publisher;

    private final UsersRepository userRepository;

    private final PrivilegesRepository privilegeRepository;


    private final com.utils.application.JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    public UsersService(ApplicationEventPublisher publisher, UsersRepository userRepository, PrivilegesRepository privilegeRepository, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RedisService redisService) {
        this.publisher = publisher;
        this.userRepository = userRepository;
        this.privilegeRepository = privilegeRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
    }

    private List<UsersResponse> registerUsers(RequestContract request) {
        if (request instanceof UsersRegisterRequest castedRequest) {
            Optional<Users> entitiesList;
            var optionalPrivilege = privilegeRepository.findByPrivilegeName(castedRequest.getPrivileges().getPrivilegeName());
            entitiesList = userRepository.findByUserCellphoneNo(getInstance().validateCellphoneNo(castedRequest.getUserCellphoneNo()));

            logger.info("entities list : {} ", entitiesList);
            if (entitiesList.isPresent()) {
                if (entitiesList.get().getUserStatus() == 0) {
                    var errorMessage = "User has already been registered, email or cellphone number not verified";
                    var resolveIssue = "Login to get verification link";
                    throw throwExceptionAndReport(new UserNotVerifiedException(errorMessage), errorMessage, resolveIssue);
                } else {
                    var errorMessage = "User has already been registered";
                    var resolveIssue = "Please login";
                    throw throwExceptionAndReport(new UsersExistsException(errorMessage), errorMessage, resolveIssue);
                }
            } else {

                if (castedRequest.getPrivileges().getId() > 4 || castedRequest.getPrivileges().getId()  < 1) {
                    var errorMessage = "AA agent do not have privilege id of : " + castedRequest.getPrivileges();
                    var resolveIssue = "use registration form to register in official website or app";
                    throw throwExceptionAndReport(new PrivilegeIdOutOfBoundException(errorMessage), errorMessage, resolveIssue);
                }

                if (castedRequest.getUserPassword().equals(castedRequest.getConfirmPassword())) {
                    Privileges privileges;
                    if(optionalPrivilege.isPresent()){
                        privileges = optionalPrivilege.get();
                    }else{
                        var errorMessage = "AA agent do not have privilege id of : " + castedRequest.getPrivileges();
                        var resolveIssue = "use registration form to register in official website or app";
                        throw throwExceptionAndReport(new PrivilegeNotFoundException(errorMessage), errorMessage, resolveIssue);
                    }

                    Users users = Users.builder()
                            .userIdentityNo(getInstance().validateIdentityNo(castedRequest.getUserIdentityNo()))
                            .userPassword(passwordEncoder.encode(getInstance().checkPasswordValidity(castedRequest.getUserPassword().trim())))
                            .userRegistrationDate(getInstance().formatDateTime(LocalDateTime.now()))
                            .userModifiedDate(getInstance().formatDateTime(LocalDateTime.now()))
                            .privileges(privileges)
                            .userStatus((short) 0)
                            .userCellphoneNo(getInstance().validateCellphoneNo(castedRequest.getUserCellphoneNo().trim()))
                            .userFullName(castedRequest.getUserFullName().trim())
                            .userEmailAddress(castedRequest.getUserEmailAddress().trim())
                            .userAge(getInstance().getValidatedAge())
                            .passwordUpdateStatus((short) 0)
                            .build();

                    try {
                        logger.info("users was successfully registered : data : {}", userRepository.save(users));

                        var responseList = mapToResponse(List.of(users));

                        if (redisService.get("ALL_USERS", UsersResponse.class) != null) {
                            logger.info("Cached data for all users deleted  : {}", redisService.delete("ALL_USERS"));
                        }

                        logger.info("User : {} successfully registered data : {}", castedRequest.getUserFullName(), responseList);
                        return responseList;

                    } catch (DataIntegrityViolationException e) {
                        var errorMessage = "User has already been registered";
                        var resolveIssue = "Some of your data is registered, contact AA for verification";
                        throw throwExceptionAndReport(new UsersExistsException(errorMessage), errorMessage, resolveIssue);
                    }
                } else {
                    var errorMessage = "Password inserted  does not match";
                    var resolveIssue = "Please ensure password and confirm password matches";
                    throw throwExceptionAndReport(new PasswordMisMatchException(errorMessage), errorMessage, resolveIssue);
                }
            }
        } else {
            var errorMessage = "request insert doesn't match user register request";
            var resolveIssue = "Please insert correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);

        }
    }



    /*
          this code is out of scope v1
     */
//    private List<UsersResponse> findUserByUsersIdentityNo() {
//        String encrypt = getInstance().validateIdentityNo(identityNocastedRequest.getUsersIdentityNo());
//        UsersResponse redisUserResponse = redisService.get(encrypt, UsersResponse.class);
//
//        if (redisUserResponse != null) {
//            logger.info("User with identity no : {} successfully found from cache, data is {}  ", redisUserResponse.getUsersIdentityNo(), redisUserResponse);
//            return List.of(redisUserResponse);
//        } else {
//            var responseList = userRepository.findByUserIdentityNo(identityNocastedRequest.getUsersIdentityNo()).stream().map(usersMapper::toDto).toList();
//
//            if (responseList.size() == 1) {
//                var jpaUserResponse = responseList.get(0);
//                redisService.set(encrypt, jpaUserResponse, 1L, TimeUnit.HOURS);
//                logger.info("User with identity no : {} successfully found from jpa, data is {}  ", redisUserResponse.getUsersIdentityNo(), jpaUserResponse);
//
//                return responseList;
//            } else {
//                var errorMessage = "User with identity no ending with XXX-XXX-XXX-" + identityNocastedRequest.getUsersIdentityNo().substring(9) + "not found";
//                var resolveIssue = "Please review the identity number inserted";
//                throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, resolveIssue);
//            }
//        }
//
//    }


    private List<UsersResponse> resetPassword(RequestContract request) {

        if (request instanceof UpdatePasswordRequest castedRequest) {
            try {
                var dbEntity = userRepository.findByToken(castedRequest.getUserToken());
                if (dbEntity.isPresent()) {
                    var user = dbEntity.get();
                    if (getInstance().checkPasswordValidity(castedRequest.getUsersPassword()).equals(getInstance().checkPasswordValidity(castedRequest.getUsersConfirmPassword()))) {
                        user.setUserPassword(passwordEncoder.encode(castedRequest.getUsersPassword()));
                        user.setUserModifiedDate(getInstance().formatDateTime(LocalDateTime.now()));
                        var updateEntity = userRepository.save(user);

                        if (redisService.get(updateEntity.getUserEmailAddress(), UsersResponse.class) != null) {
                            logger.info("user with email address {} successfully successfully removed from cache", redisService.delete(updateEntity.getUserEmailAddress()));
                        }
                        logger.info("user with email address {} successfully updated password", updateEntity.getUserEmailAddress());
                        return mapToResponse(List.of(updateEntity));
                    } else {
                        var errorMessage = "new password and confirm password don't match";
                        var resolveIssue = "Please confirm your new password and confirmation password";
                        throw throwExceptionAndReport(new PasswordMisMatchException(errorMessage), errorMessage, resolveIssue);
                    }
                } else {
                    var errorMessage = "User doesn't exists, check your email address";
                    var resolveIssue = "Please check your email address, re-enter email address";
                    throw throwExceptionAndReport(new UserEmailDoesNotExistException(errorMessage), errorMessage, resolveIssue);
                }
            } catch (NullPointerException e) {
                var errorMessage = "Update password castedRequest is null";
                var resolveIssue = "contact AA administrator";
                throw throwExceptionAndReport(new NullRequestException(errorMessage), errorMessage, resolveIssue);
            }
        } else {
            var errorMessage = "request insert doesn't match update passord request";
            var resolveIssue = "Please insert correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);


        }
    }

    private List<UsersResponse> mapToResponse(List<Users> list) {
        return list.parallelStream().map(s -> UsersResponse
                .builder()
                .usersAge(s.getUserAge())
                .id(s.getId())
                .usersStatus(s.getUserStatus())
                .usersEmailAddress(s.getUserEmailAddress())
                .usersRegistrationDate(s.getUserRegistrationDate())
                .usersModifiedDate(s.getUserModifiedDate())
                .usersFullName(s.getUserFullName())
                .usersIdentityNo(s.getUserIdentityNo())
                .cellphoneNo(s.getUserCellphoneNo())
                .privileges(s.getPrivileges().getId())
                .token(s.getToken())
                .updatePasswordStatus(s.getPasswordUpdateStatus())
                .privilege(s.getPrivileges())
                .build()).toList();

    }

    private List<UsersResponse> findUserById(RequestContract request) {

        if (request instanceof FindByIdRequest castedRequest) {
            String encrypt;
            try {
                encrypt = castedRequest.getId().toString();

                UsersResponse redisUserResponse = redisService.get(encrypt, UsersResponse.class);

                if (redisUserResponse != null) {
                    logger.info("user with id {} successfully retrieved from cache data : {}", redisUserResponse.getId(), redisUserResponse);

                    return List.of(redisUserResponse);
                } else {
                    var responseList = userRepository.findById(castedRequest.getId());

                    if (responseList.isPresent()) {
                        var users = responseList.get();

                        var jpaUserResponse = mapToResponse(List.of(users));

                        redisService.set(encrypt, jpaUserResponse.get(0), 1L, TimeUnit.HOURS);
                        logger.info("cached data : {}", redisService.get(encrypt, UsersResponse.class));
                        logger.info("user with id {} successfully retrieved from jpa data : {}", jpaUserResponse.get(0).getId(), jpaUserResponse);
                        return jpaUserResponse;
                    } else {
                        var errorMessage = "user id : " + castedRequest.getId() + " not found";
                        var resolveIssue = "Please review id inserted";
                        throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, resolveIssue);
                    }
                }
            } catch (NullPointerException e) {
                var errorMessage = "Find by id castedRequest is null ";
                var resolveIssue = "Contact AA System administrator";
                throw throwExceptionAndReport(new RuntimeException(errorMessage), errorMessage, resolveIssue);
            }
        } else {
            var errorMessage = "request insert doesn't match find by id request";
            var resolveIssue = "Please insert correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);

        }

    }


    private List<UsersResponse> findAllUsers() {
        var redisResponse = redisService.get("ALL_USERS", List.class);
        if (redisResponse != null) {
            var returnRedisResponse = redisService.safeCastList(redisResponse, UsersResponse.class);
            logger.info("get all users from cache, data from redis : {}", returnRedisResponse);
            return returnRedisResponse;
        } else {
            var jpaResponse = mapToResponse(userRepository.findAll());

            redisService.set("ALL_USERS", jpaResponse, 1L, TimeUnit.HOURS);
            var redisResponseChecker = redisService.get("ALL_USERS", List.class);
            logger.info("cached data for all users from jpa : {}", redisService.safeCastList(redisResponseChecker, UsersResponse.class));
            return jpaResponse;
        }
    }

    private List<UsersResponse> findAllUsersByName(RequestContract request) {

        if (request instanceof UsersFullNameRequest castedRequest) {
            try {
                String encrypt = castedRequest.getUsersFullName();
                UsersResponse redisUserResponse = redisService.get(encrypt, UsersResponse.class);
                if (redisUserResponse != null) {
                    logger.info("user with name {} successfully retrieved from cache data : {}", redisUserResponse.getUsersFullName(), redisUserResponse);

                    return List.of(redisUserResponse);
                } else {
                    var responseList = userRepository.findByUserFullName(castedRequest.getUsersFullName());

                    if (responseList.isPresent()) {
                        var users = responseList.get();
                        var jpaUserResponse = mapToResponse(List.of(users));
                        redisService.set(encrypt, jpaUserResponse.get(0), 1L, TimeUnit.HOURS);
                        logger.info("user with name {} successfully retrieved from jpa data : {}", jpaUserResponse.get(0).getUsersFullName(), jpaUserResponse);

                        return jpaUserResponse;
                    } else {
                        var errorMessage = "user with full name :" + castedRequest.getUsersFullName() + " not found";
                        var resolveIssue = "Please review the full name inserted";
                        throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, resolveIssue);
                    }
                }

            } catch (NullPointerException e) {
                var errorMessage = "Full name castedRequest is null";
                var resolveIssue = "Contact AA Administrator";
                throw throwExceptionAndReport(new RuntimeException(errorMessage), errorMessage, resolveIssue);
            }
        } else {
            var errorMessage = "request insert doesn't match find by full name request";
            var resolveIssue = "Please insert correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);
        }


    }

    private boolean passwordStatus;


    private List<UsersResponse> login(RequestContract request) {

        if (request instanceof LoginRequest castedRequest) {
            try {
                var encrypt = castedRequest.getUsersEmailAddress();
                UsersResponse redisUserResponse = redisService.get(encrypt, UsersResponse.class);
                UsersResponse jpaUserResponse;
                boolean redisStatus;
                if (redisUserResponse != null) {
                    logger.info("redis executing...");
                    jpaUserResponse = null;
                    if (redisUserResponse.getUsersStatus() == 0) {
                        var errorMessage = "User already got email to verify account";
                        var resolveIssue = "check emails and click the link from email : verify@aa.com. New link will be generated after Hour";
                        throw throwExceptionAndReport(new VerifyEmailAddressException(errorMessage), errorMessage, resolveIssue);
                    }

                    redisStatus = true;


                } else {
                    logger.info("Jpa executing...");
                    redisStatus = false;


                    var optionalEntity = userRepository.findByUserEmailAddress(castedRequest.getUsersEmailAddress());


                    if (optionalEntity.isPresent()) {
                        logger.info("email address {} found", castedRequest.getUsersEmailAddress());


                        var users = optionalEntity.get();
                        var jwt = jwtService.generateToken(optionalEntity.get());
                        users.setToken(jwt);
                        logger.info("JWT Token : {}", jwt);
                        logger.info("User with set token  : {}", users);


                        var userWithToken = userRepository.save(users);
                        logger.info("User with set token from db : {}", userWithToken);

                        jpaUserResponse = mapToResponse(List.of(userWithToken)).get(0);
// send mail when status is 0, pub sub pattern will be used
                        if (optionalEntity.get().getUserStatus() == 0) {
                            publisher.publishEvent(VerifyCustomerEvent
                                    .builder()
                                    .emailFrom("softwareaa65@gmail.com")
                                    .emailTo(jpaUserResponse.getUsersEmailAddress())
                                    .name(jpaUserResponse.getUsersFullName())
                                    .token(jpaUserResponse.getToken())
                                    .privilegeId(optionalEntity.get().getPrivileges().getId())
                                    .build()
                            );

                            logger.info("user : {} has not been verified, email sent to verify customer", jpaUserResponse.getUsersFullName());
                        } else if (optionalEntity.get().getUserStatus() == 1) {
                            passwordStatus = true;
                            logger.info("user : {} has  been verified,trying to login...", jpaUserResponse.getUsersFullName());

                        } else {

                            var errorMessage = "log in failed due to invalid user status ";
                            var resolveIssue = "Contact AA Administrator";
                            throw throwExceptionAndReport(new InvalidUserStatusException(errorMessage), errorMessage, resolveIssue);
                        }
                    } else {
                        logger.info("email address {} not found", castedRequest.getUsersEmailAddress());
                        jpaUserResponse = null;
                        passwordStatus = false;
                    }

                }

                try {
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(castedRequest.getUsersEmailAddress(), castedRequest.getUsersPassword()));
                    if (redisStatus) {
                        logger.info("user with email {} successfully logged in using cache data : {}", redisUserResponse.getUsersEmailAddress(), redisUserResponse);

                        return List.of(redisUserResponse);
                    } else {

                        logger.info("user with email {} successfully logged in using jpa data : {}", castedRequest.getUsersEmailAddress(), jpaUserResponse);

                        redisService.set(encrypt, jpaUserResponse, 1L, TimeUnit.HOURS);

                        logger.info("cached login data : {}", redisService.get(encrypt, UsersResponse.class));


                        return List.of(jpaUserResponse);
                    }
                } catch (AuthenticationException e) {
                    if (redisStatus) {

                        logger.info("delete cached data for login : {}", redisService.delete(encrypt));
                        var errorMessage = "cached data shows change of password ";
                        var resolveIssue = "please log in again";
                        throw throwExceptionAndReport(new CachedUsersPasswordChangedException(errorMessage), errorMessage, resolveIssue);
                    } else {
                        if (passwordStatus) {
                            var errorMessage = UsersControllerAdvice.setMessage("password inserted is incorrect");
                            var resolveIssue = "please provide correct password or update password";
                            throw throwExceptionAndReport(new UsersPasswordIncorrectException(errorMessage), errorMessage, resolveIssue);
                        } else {
                            var errorMessage = UsersControllerAdvice.setMessage("email address " + castedRequest.getUsersEmailAddress() + " not found, verify your email or register");
                            var resolveIssue = "Enter correct email address or register using the email entered";
                            throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, resolveIssue);
                        }
                    }

                }
            } catch (NullPointerException e) {
                var errorMessage = UsersControllerAdvice.setMessage("Login castedRequest is null");
                var resolveIssue = "Contact AA Administrator";
                throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, resolveIssue);
            }
        } else {
            var errorMessage = UsersControllerAdvice.setMessage("Request is not matching Login request");
            var resolveIssue = "Please provide the correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);
        }

    }

    @Override
    public List<UsersResponse> call(String serviceRunner, RequestContract request) {

        return switch (serviceRunner) {
            case "registerUsers" -> this.registerUsers(request);
            case "getAllUsers" -> this.findAllUsers();
            case "getUsersByFullName" -> this.findAllUsersByName(request);
            //code is out of scope v1
//                case "getUsersByIdentityNo":
//                    return this.findUserByUsersIdentityNo();
            case "getUsersById" -> this.findUserById(request);
            case "userLogin" -> this.login(request);
            case "reset-password" -> this.resetPassword(request);
            case "verifyUser" -> this.verifyCustomer(request);
            case "forgot-password" -> this.forgotPassword(request);
            case "verifyPasswordUpdate" -> this.verifyPasswordUpdate(request);
            default -> throw new ServiceHandlerException("Failed execute service due to incorrect service string");
        };

    }


    private List<UsersResponse> forgotPassword(RequestContract request) {
        if (request instanceof FindByEmailRequest castedRequest) {
            var encrypt = castedRequest.getEmailAddress();

            if (redisService.get("Reset-" + encrypt, UsersResponse.class) != null) {
                var errorMessage = "email sent to reset password sent please check your email to continue updating your password";
                var resolveIssue = "Please check emails and click the reset link or wait hour to get new link";
                throw throwExceptionAndReport(new ResetPasswordSessionException(errorMessage), errorMessage, resolveIssue);

            } else {
                Optional<Users> user = userRepository.findByUserEmailAddress(encrypt);

                if (user.isPresent()) {
                    var jpaEntity = user.get();

                    var jwt = jwtService.generateToken(jpaEntity);
                    jpaEntity.setToken(jwt);
                    var jpaResponse = mapToResponse(List.of(userRepository.save(jpaEntity)));
                    publisher.publishEvent(VerifyUpdatePasswordEvent
                            .builder()
                            .emailFrom("softwareaa65@gmail.com")
                            .emailTo(jpaResponse.get(0).getUsersEmailAddress())
                            .name(jpaResponse.get(0).getUsersFullName())
                            .token(jpaResponse.get(0).getToken())
                            .build()
                    );
                    redisService.set("Reset-" + encrypt, jpaResponse.get(0), 1L, TimeUnit.HOURS);
                    if (redisService.get(encrypt, UsersResponse.class) != null)
                        redisService.delete(encrypt);
                    return jpaResponse;
                } else {
                    var errorMessage = "user with email:" + castedRequest.getEmailAddress() + " not found";
                    var resolveIssue = "Please provide correct email";
                    throw throwExceptionAndReport(new UserNotFoundException(errorMessage), errorMessage, resolveIssue);

                }


            }
        } else {
            var errorMessage = UsersControllerAdvice.setMessage("Request is not matching find by email request");
            var resolveIssue = "Please provide the correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);
        }
    }

    private List<UsersResponse> verifyPasswordUpdate(RequestContract request) {
        if (request instanceof FindByTokenRequest castedRequest) {
            Optional<Users> user = userRepository.findByToken(castedRequest.getToken());
            if (user.isPresent()) {
                var verifiedUser = user.get();
                verifiedUser.setPasswordUpdateStatus((short) 1);
                verifiedUser.setPreviousPassword(verifiedUser.getPassword());
                verifiedUser.setUserModifiedDate(getInstance().formatDateTime(LocalDateTime.now()));

                logger.info("User : {} has been successfully verified", verifiedUser.getUserFullName());
                redisService.delete(verifiedUser.getUserEmailAddress());

                return mapToResponse(List.of(userRepository.save(verifiedUser)));

            } else {
                var errorMessage = UsersControllerAdvice.setMessage("User token to verify customer has expired");
                var resolveIssue = "Please log in again to get new token in your mail";
                throw throwExceptionAndReport(new VerificationTokenIncorrectException(errorMessage), errorMessage, resolveIssue);
            }
        } else {
            var errorMessage = UsersControllerAdvice.setMessage("Request is not matching find token request");
            var resolveIssue = "Please provide the correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);
        }
    }


    private List<UsersResponse> verifyCustomer(RequestContract request) {
        if (request instanceof FindByTokenRequest castedRequest) {
            Optional<Users> user = userRepository.findByToken(castedRequest.getToken());

            if (user.isPresent()) {
                var verifiedUser = user.get();
                verifiedUser.setUserStatus((short) 1);
                verifiedUser.setPasswordUpdateStatus((short) 1);
                verifiedUser.setUserModifiedDate(getInstance().formatDateTime(LocalDateTime.now()));
                logger.info("User : {} has been successfully verified", verifiedUser.getUserFullName());
                redisService.delete(verifiedUser.getUserEmailAddress());

                return mapToResponse(List.of(userRepository.save(verifiedUser)));
            } else {
                var errorMessage = UsersControllerAdvice.setMessage("User token to verify customer has expired");
                var resolveIssue = "Please click forgot password again";
                throw throwExceptionAndReport(new VerificationTokenIncorrectException(errorMessage), errorMessage, resolveIssue);
            }
        } else {
            var errorMessage = UsersControllerAdvice.setMessage("Request is not matching find token request");
            var resolveIssue = "Please provide the correct request";
            throw throwExceptionAndReport(new IncorrectRequestException(errorMessage), errorMessage, resolveIssue);
        }
    }


}
