package com.example.common.global.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class RedisUtils {
    private final RedisTemplate<String, Long> redisTemplate;

    public Long getData(String key){
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setData(String key, Long value){
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void setDateWithExpiration(String key, Long value, Duration duration) {
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, duration);
    }

    public void deleteData(String key){
        redisTemplate.delete(key);
    }
}
