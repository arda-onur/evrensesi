package com.evrensesi.evrensesi.utility.redis;

import lombok.RequiredArgsConstructor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.args.ExpiryOption;

@RequiredArgsConstructor
public class FixedWindowRateLimiter {
    private Jedis jedis;
    private int windowSize = 10;
    private int  limit = 5;

    public FixedWindowRateLimiter(Jedis jedis) {
        this.jedis = jedis;
    }

    public boolean isAllowed(String username) {
        String key = "rate_limit:" + username;
        String currentCounterString = jedis.get(key);
        int currentCounter = currentCounterString == null ? 0 : Integer.parseInt(currentCounterString);

        boolean isAllowed = currentCounter < limit;

        if (isAllowed) {
            Transaction transaction = jedis.multi();
            transaction.incr(key);
            transaction.expire(key, windowSize, ExpiryOption.NX);
            transaction.exec();
        }
        return isAllowed;
    }
}
