package com.retails.followers.application.service;


import com.retails.application.entity.RetailCompany;
import com.retails.application.repository.RetailCompanyRepository;
import com.retails.followers.application.dto.GetCompanyFollowers;
import com.retails.followers.application.dto.GetFollowedCompaniesRequest;
import com.retails.followers.application.dto.UserFollowersRetailRequest;
import com.retails.followers.application.dto.UserFollowersRetailResponse;
import com.retails.followers.application.entities.UserFollowersRetailCompany;
import com.retails.followers.application.exceptions.*;
import com.retails.followers.application.repository.UserFollowersRetailRepository;
import com.users.application.entities.Users;
import com.users.application.exceptions.UsersExistsException;
import com.users.application.repository.UsersRepository;
import com.utils.application.RedisService;
import com.utils.application.RequestContract;
import com.utils.application.globalExceptions.IncorrectRequestException;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.utils.application.ExceptionHandler.throwExceptionAndReport;

@Service
@Transactional
public class UserFollowersRetailService {
private final Logger logger = LoggerFactory.getLogger(UserFollowersRetailService.class);

    private String traceId() {
        String trace = UUID.randomUUID().toString();
        MDC.put("traceId", trace);
        return trace;
    }
    private final UserFollowersRetailRepository followersRepo;
    private final UsersRepository usersRepo;
    private final RetailCompanyRepository retailRepo;
    private final RedisService redisService;

    public UserFollowersRetailService(
            UserFollowersRetailRepository followersRepo,
            UsersRepository usersRepo,
            RetailCompanyRepository retailRepo
            , RedisService redisService) {
        this.redisService = redisService;
        this.followersRepo = followersRepo;
        this.usersRepo = usersRepo;
        this.retailRepo = retailRepo;
    }

    private List<UserFollowersRetailResponse> followCompany(RequestContract request) {

        String trace = traceId();
        long start = System.currentTimeMillis();

        logger.info("[TRACE:{}] Follow request received", trace);

        if (!(request instanceof UserFollowersRetailRequest castedRequest)) {
            String msg = "Invalid request type for follow";
            logger.error("[TRACE:{}] {}", trace, msg);
            throw throwExceptionAndReport(new IncorrectRequestException(msg), msg, "Fix request body");
        }

        logger.debug("[TRACE:{}] UserId={}, RetailCompanyId={}",
                trace, castedRequest.getFk_user_id(), castedRequest.getFk_retail_company_id());

        Users user = usersRepo.findById(castedRequest.getFk_user_id())
                .orElseThrow(() -> {
                    String msg = "User not found while following company";
                    logger.warn("[TRACE:{}] {}", trace, msg);
                    return throwExceptionAndReport(new UserFollowingRetailNotFoundException(msg), msg, "Verify user id");
                });

        RetailCompany company = retailRepo.findById(castedRequest.getFk_retail_company_id())
                .orElseThrow(() -> {
                    String msg = "Retail company not found while following";
                    logger.warn("[TRACE:{}] {}", trace, msg);
                    return throwExceptionAndReport(new RetailCompanyFollowedNotFoundException(msg), msg, "Verify company id");
                });

        followersRepo.findByUserIdAndCompanyRetailId(
                castedRequest.getFk_user_id(),
                castedRequest.getFk_retail_company_id()
        ).ifPresent(existing -> {

            if (existing.getStatus() == 1) {
                String msg = "User already follows this company";
                logger.info("[TRACE:{}] {}", trace, msg);
                throw throwExceptionAndReport(new UserAlreadyFollowedRetailCompanyException(msg), msg, "Already followed");
            }

            logger.info("[TRACE:{}] Re-activating follow relationship", trace);

            existing.setStatus((byte) 1);
            existing.setRegisteredFollowDate(formatDateTime(LocalDateTime.now()));
            existing.setRegisteredUnfollowingDate(null);

            followersRepo.save(existing);
        });

        // Cache invalidation logs
        String userCacheKey = "user:follows:" + castedRequest.getFk_user_id();
        String companyCacheKey = "company:followers:" + castedRequest.getFk_retail_company_id();

        redisService.delete(userCacheKey);
        redisService.delete(companyCacheKey);

        logger.debug("[TRACE:{}] Redis invalidated: {}, {}", trace, userCacheKey, companyCacheKey);

        UserFollowersRetailCompany follow = UserFollowersRetailCompany.builder()
                .user(user)
                .companyRetail(company)
                .registeredFollowDate(formatDateTime(LocalDateTime.now()))
                .status((byte) 1)
                .build();

        UserFollowersRetailCompany saved = followersRepo.save(follow);

        long duration = System.currentTimeMillis() - start;

        logger.info("[TRACE:{}] Follow success | followId={} | duration={}ms",
                trace, saved.getId(), duration);

        return List.of(mapToResponse(saved));
    }

    private List<UserFollowersRetailResponse> unfollowCompany(RequestContract request) {
        String trace = traceId();
        long start = System.currentTimeMillis();

        logger.info("[TRACE:{}] UnfollowCompany request received", trace);


        if (request instanceof UserFollowersRetailRequest castedRequest) {
            Long userId = castedRequest.getFk_user_id();
            Long companyId = castedRequest.getFk_retail_company_id();

            logger.debug("[TRACE:{}] userId={} | companyId={}", trace, userId, companyId);

            UserFollowersRetailCompany follow = followersRepo
                    .findByUserIdAndCompanyRetailId(castedRequest.getFk_user_id(), castedRequest.getFk_retail_company_id())
                    .orElseThrow(() -> {
                        String msg = "Follow record not found for unfollow action";
                        logger.warn("[TRACE:{}] {}", trace, msg);
                        return throwExceptionAndReport(
                                new FollowingRecordNotFoundException(msg),
                                msg,
                                "Ensure follow record exists"
                        );}
                    );

            if (follow.getStatus() == 1) {
                logger.info("[TRACE:{}] Updating follow record to status=0", trace);

                follow.setStatus((byte) 0);
                follow.setRegisteredUnfollowingDate(formatDateTime(LocalDateTime.now()));
                // ❌ Clear cache after follow
                redisService.delete("user:follows:" + castedRequest.getFk_user_id());
                redisService.delete("company:followers:" + castedRequest.getFk_retail_company_id());

                return List.of(mapToResponse(followersRepo.save(follow)));
            }else{
                String msg = "Unfollow failed — already unfollowed (status=0)";
                logger.warn("[TRACE:{}] {}", trace, msg);
                throw throwExceptionAndReport(
                        new UnfollowingFailedStatusIs0Exception(msg),
                        msg,
                        "Ensure follow status is active before unfollow");
            }
        } else {
            String msg = "Invalid request type for unfollowCompany";
            logger.error("[TRACE:{}] {}", trace, msg);
            throw throwExceptionAndReport(new IncorrectRequestException(msg), msg, "Fix request type");
        }
    }

    private String formatDateTime(LocalDateTime issueDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return issueDate.format(formatter);
    }

    private UserFollowersRetailResponse mapToResponse(UserFollowersRetailCompany follow) {
        return UserFollowersRetailResponse.builder()
                .followId(follow.getId())
                .userId(follow.getUser().getId())
                .companyId(follow.getCompanyRetail().getId())
                .companyName(follow.getCompanyRetail().getRetailCompanyName())
                .status(follow.getStatus())
                .followedAt(follow.getRegisteredFollowDate())
                .build();
    }

    private List<UserFollowersRetailResponse> getFollowedCompanies(RequestContract request) {
        String trace = traceId();

        if (!(request instanceof GetFollowedCompaniesRequest castedRequest)) {
            String msg = "Invalid request type for getFollowedCompanies";
            logger.error("[TRACE:{}] {}", trace, msg);
            throw throwExceptionAndReport(new IncorrectRequestException(msg), msg, "Fix request");
        }

        String redisKey = "user:follows:" + castedRequest.fk_user_id;

        logger.debug("[TRACE:{}] Checking Redis cache key={}", trace, redisKey);

        Object cached = redisService.get(redisKey, UserFollowersRetailResponse.class);

        if (cached != null) {
            logger.info("[TRACE:{}] Redis HIT for key={}", trace, redisKey);
            return (List<UserFollowersRetailResponse>) cached;
        }

        logger.info("[TRACE:{}] Redis MISS — querying DB", trace);

        List<UserFollowersRetailResponse> list = followersRepo.findByUserId(castedRequest.fk_user_id)
                .parallelStream()
                .map(this::mapToResponse)
                .toList();

        redisService.set(redisKey, list, 1L, TimeUnit.HOURS);

        logger.info("[TRACE:{}] Cached {} records to Redis key={}", trace, list.size(), redisKey);

        return list;

    }

    private List<UserFollowersRetailResponse> getCompanyFollowers(RequestContract request) {
        String trace = traceId();
        long start = System.currentTimeMillis();

        logger.info("[TRACE:{}] GetCompanyFollowers request received", trace);

        if (request instanceof GetCompanyFollowers castedRequest) {
            String redisKey = "company:followers:" + castedRequest.fk_retail_company_id;
            logger.debug("[TRACE:{}] CompanyId={} | RedisKey={}", trace, castedRequest.fk_retail_company_id, redisKey);

            var redisResponse =redisService.get(redisKey,UserFollowersRetailResponse.class);
                  if(redisResponse != null){
                      logger.info("[TRACE:{}] Redis HIT for key={}", trace, redisKey);

                      long duration = System.currentTimeMillis() - start;
                      logger.info("[TRACE:{}] getCompanyFollowers completed in {}ms (CACHE)", trace, duration);

                      return List.of(redisResponse);
                  }else {
                      logger.info("[TRACE:{}] Redis MISS — querying database", trace);

                      List<UserFollowersRetailResponse> list = followersRepo.findByCompanyRetailId(castedRequest.fk_retail_company_id)
                              .parallelStream()
                              .map(this::mapToResponse)
                              .toList();
                      redisService.set(redisKey,list.getFirst(),1L, TimeUnit.HOURS);
                      return list;
                  }
        } else {
            String msg = "Invalid request type for getCompanyFollowers";
            logger.error("[TRACE:{}] {}", trace, msg);
            throw throwExceptionAndReport(new IncorrectRequestException(msg), msg, "Fix request type");
        }
    }

    public List<UserFollowersRetailResponse> call(String serviceRunner,RequestContract request) {
        return switch (serviceRunner) {
            case "follow" -> this.followCompany(request);
            case "unfollow" -> this.unfollowCompany(request);
            case "getFollowedCompanies" -> this.getFollowedCompanies(request);
            case "getCompanyFollowers" -> this.getCompanyFollowers(request);
            default -> throw new NullPointerException("service validator is empty");
        };
    }
}