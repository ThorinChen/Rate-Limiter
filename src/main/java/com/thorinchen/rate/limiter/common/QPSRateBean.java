
package com.thorinchen.rate.limiter.common;

import com.thorinchen.rate.limiter.annotation.QPSRate;
import org.apache.commons.lang3.StringUtils;


public final class QPSRateBean extends RateLimitBean {

    private int acquire;

    public QPSRateBean(QPSRate qpsRate, RateLimitBean father) {
        setKey(StringUtils.isEmpty(qpsRate.key()) ? father.getKey() : qpsRate.key());
        setRate(qpsRate.rate() > 0 ? qpsRate.rate() : father.getRate());
        setStrategy(qpsRate.strategy());
        setAcquire(qpsRate.acquire());
    }

    public int getAcquire() {
        return acquire;
    }

    public void setAcquire(int acquire) {
        this.acquire = acquire;
    }
}