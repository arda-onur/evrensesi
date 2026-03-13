package com.arda.evrensesi.service;

public interface RateLimiterService {

    boolean isValidRequest(String ip, int limit, int duration);
}
