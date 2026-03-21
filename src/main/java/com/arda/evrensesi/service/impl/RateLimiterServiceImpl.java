package com.arda.evrensesi.service.impl;

import com.arda.evrensesi.service.RateLimiterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.format.annotation.DurationFormat;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RateLimiterServiceImpl implements RateLimiterService {

    private final StringRedisTemplate redisTemplate;

    public RateLimiterServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isValidRequest(String ip, int limit, int duration) {
        String key = "rate_limit:" + ip;

        Long count = redisTemplate.opsForValue().increment(key);

        if (count == 1) {
            redisTemplate.expire(key, duration, TimeUnit.SECONDS);
            log.info("Rate limit window started. ip={}, duration={}s", ip, duration);
        }


        boolean isLimitExceeded = count <= limit;

        return isLimitExceeded;
    }
}

