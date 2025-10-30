package com.aa.AA.utils.executors;

import com.aa.AA.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.Callable;

public interface Execute<T> extends Callable<T> {

    void setCache(@Autowired RedisService redisService);

    void setEncodeCacheKey(@Autowired PasswordEncoder passwordEncoder);
}
