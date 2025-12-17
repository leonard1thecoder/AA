package com.utils.application;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private RedisTemplate redisTemplate;

    @Autowired
    public RedisService(@Autowired RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> T get(String key, Class<T> response){
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(obj.toString(),response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        catch (NullPointerException e ){
            return null;
        }
    }

    public  <T> T safeCast(Object redisResponse, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(redisResponse, clazz);
    }

    public  <T> List<T> safeCastList(Object redisResponse, Class<T> elementType) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(redisResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, elementType));
    }

    public void set (String key, Object obj, Long ttl, TimeUnit timeUnit) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonValue = mapper.writeValueAsString(obj);
            redisTemplate.opsForValue().set(key,jsonValue,ttl,timeUnit);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Object delete(String key){

            return redisTemplate.opsForValue().getAndDelete(key);

    }
}
