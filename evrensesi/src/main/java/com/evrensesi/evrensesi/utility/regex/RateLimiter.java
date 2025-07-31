package com.evrensesi.evrensesi.utility.regex;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RateLimiter {
    private final RedisTemplate<String, String> redisTemplate;

    public RateLimiter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isRateLimited(String userId) {
        String key = "user:" + userId + ":rate_limit";
        try {
            Long current = redisTemplate.opsForValue().increment(key);
            if (current == 1) {
                redisTemplate.expire(key, 5, TimeUnit.SECONDS);
            }
            return current > 1;
        } catch (Exception e) {
            return false;
        }
    }
}
