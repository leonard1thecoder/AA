package com.utils.application.configurations;


import com.utils.application.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(
            ) {
        return new LettuceConnectionFactory("redis", 6379);
    }

    @Bean
    public RedisTemplate redisTemplate(LettuceConnectionFactory connectionFactory){
     RedisTemplate redisTemplate = new RedisTemplate<>();
     redisTemplate.setConnectionFactory(connectionFactory);
     redisTemplate.setKeySerializer(new StringRedisSerializer());
     redisTemplate.setValueSerializer(new StringRedisSerializer());
        
     return redisTemplate;
    }

    @Bean
    public RedisService redisService(RedisTemplate redisTemplate){
        return new RedisService(redisTemplate);
    }

}
