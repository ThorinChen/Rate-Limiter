
package com.thorinchen.rate.limiter.core.acquire;

import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class QPSLimitAcquirable extends RateLimiterAcquirable {

    private static final Logger logger = LoggerFactory.getLogger(QPSLimitAcquirable.class);

    private int acquire;

    public QPSLimitAcquirable(RateLimiter rateLimiter, int acquire) {
        super(rateLimiter);
        this.acquire = acquire;
    }

    public int getAcquire() {
        return acquire;
    }

    @Override
    public void acquire(Object[] objects) {
        int acquire = getAcquire();
        logger.debug("acquire for {}", acquire);
        getRateLimiter().acquire(acquire);
    }
}