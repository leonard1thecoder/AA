package com.users.application.threadsExecutors;

import com.utils.application.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.Callable;

public interface Execute<T> extends Callable<T> {
    void setCache(@Autowired RedisService redisService);

    void setEncodeCacheKey(@Autowired PasswordEncoder passwordEncoder);
}
