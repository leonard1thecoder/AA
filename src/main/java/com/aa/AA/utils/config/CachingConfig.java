package com.aa.AA.utils.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class CachingConfig {


    private CacheManager cache;

    public CachingConfig(@Autowired CacheManager cache) {
        this.cache = cache;
    }

    public Cache cache(String cacheName){
        var cache = this.cache.getCache(cacheName);


        if (cache != null ){
            System.out.println(cache);
            return cache;
        }else{
            System.out.println("no cache : "+ cacheName);
            return null;
        }

    }
}
