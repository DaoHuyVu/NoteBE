package com.example.todoapp.config;

import com.example.todoapp.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@SuppressWarnings("unused")
@EnableCaching
public class RedisConfig {
    @Value("${accessTokenExpiration}")
    private Long accessTokenExpiration;
    @Bean
    public RedisCacheConfiguration cacheConfiguration(){
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeValuesWith(
                        RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer())
                );
    }
    @Bean
    public LettuceConnectionFactory connectionFactory(){
        return new LettuceConnectionFactory();
    }
    @Bean
    public RedisCacheManager redisCacheManager(){
        return RedisCacheManager.builder(connectionFactory())
                .withCacheConfiguration(
                        Constants.ACCESS_TOKEN,
                        cacheConfiguration().entryTtl(Duration.ofMillis(accessTokenExpiration))
                )
                .cacheDefaults(
                        cacheConfiguration()
                                .disableCachingNullValues()
                                .entryTtl(Duration.ofMinutes(10))
                )
                .build();
    }
}
