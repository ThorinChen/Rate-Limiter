
package com.thorinchen.rate.limiter.core;

import com.thorinchen.rate.limiter.common.RateLimitBean;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class LimitHolderDescriptor {

    private String limitKey;

    private String methodKey;

    private double rate;

    private RateLimitStrategy strategy = RateLimitStrategy.USE_MIN;

    public LimitHolderDescriptor(RateLimitBean limitBean, String methodKey) {
        this(limitBean.getKey(), methodKey, limitBean.getRate(), limitBean.getStrategy());
    }

    public LimitHolderDescriptor(String limitKey, String methodKey, double rate, RateLimitStrategy strategy) {
        this.limitKey = limitKey;
        this.methodKey = methodKey;
        this.rate = rate;
        this.strategy = strategy;
    }

    public String getLimitKey() {
        return limitKey;
    }

    public String getMethodKey() {
        return methodKey;
    }

    public double getRate() {
        return rate;
    }

    public RateLimitStrategy getStrategy() {
        return strategy;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}