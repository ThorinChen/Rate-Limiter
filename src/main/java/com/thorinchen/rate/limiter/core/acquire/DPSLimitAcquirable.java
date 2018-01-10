
package com.thorinchen.rate.limiter.core.acquire;

import com.google.common.util.concurrent.RateLimiter;
import com.thorinchen.rate.limiter.core.converter.IRateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DPSLimitAcquirable extends RateLimiterAcquirable {

    private static final Logger logger = LoggerFactory.getLogger(DPSLimitAcquirable.class);

    private IRateConverter converter;

    public DPSLimitAcquirable(RateLimiter rateLimiter, IRateConverter converter) {
        super(rateLimiter);
        this.converter = converter;
    }

    public IRateConverter getConverter() {
        return converter;
    }

    @Override
    public void acquire(Object[] objects) {
        int acquire = getConverter().acquire(objects);
        logger.debug("acquire for {}",acquire);
        getRateLimiter().acquire(acquire);
    }
}