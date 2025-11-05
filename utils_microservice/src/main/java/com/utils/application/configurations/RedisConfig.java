package com.utils.application.configurations;


import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory){
     RedisTemplate redisTemplate = new RedisTemplate<>();
     redisTemplate.setConnectionFactory(connectionFactory);
     redisTemplate.setKeySerializer(new StringRedisSerializer());
     redisTemplate.setValueSerializer(new StringRedisSerializer());

     return redisTemplate;
    }

}
