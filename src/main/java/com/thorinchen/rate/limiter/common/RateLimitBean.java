
package com.thorinchen.rate.limiter.common;

import com.thorinchen.rate.limiter.annotation.RateLimit;
import com.thorinchen.rate.limiter.core.RateLimitStrategy;
import org.apache.commons.lang3.StringUtils;


public class RateLimitBean {
    private String key;
    private double rate;
    private RateLimitStrategy strategy;

    public RateLimitBean() {
    }

    public RateLimitBean(RateLimit limit, Class<?> clazz) {
        this(StringUtils.isEmpty(limit.key()) ? clazz.getName() : limit.key(), limit.rate(), limit.strategy());
    }

    public RateLimitBean(String key, double rate, RateLimitStrategy strategy) {
        this.key = key;
        this.rate = rate;
        this.strategy = strategy;
    }

    public String getKey() {
        return key;
    }

    public double getRate() {
        return rate;
    }

    public RateLimitStrategy getStrategy() {
        return strategy;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setStrategy(RateLimitStrategy strategy) {
        this.strategy = strategy;
    }

    public RateLimitBean cloneOne() {
        RateLimitBean clone = new RateLimitBean();
        clone.setKey(getKey());
        clone.setRate(getRate());
        clone.setStrategy(getStrategy());
        return clone;
    }
}