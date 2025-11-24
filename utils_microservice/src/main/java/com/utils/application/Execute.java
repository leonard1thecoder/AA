package com.utils.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
public interface Execute<T> extends Callable<T> {
    void setCache(@Autowired RedisService redisService);

    void setEncodeCacheKey(@Autowired PasswordEncoder passwordEncoder);
}
