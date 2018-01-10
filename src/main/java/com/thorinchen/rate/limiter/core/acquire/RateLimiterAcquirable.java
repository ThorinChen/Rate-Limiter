
package com.thorinchen.rate.limiter.core.acquire;

import com.google.common.util.concurrent.RateLimiter;


public abstract class RateLimiterAcquirable implements IRLimitAcquirable {

    private RateLimiter rateLimiter;

    public RateLimiterAcquirable(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }
}