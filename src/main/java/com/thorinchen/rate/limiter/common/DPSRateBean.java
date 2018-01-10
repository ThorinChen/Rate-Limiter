
package com.thorinchen.rate.limiter.common;

import com.thorinchen.rate.limiter.annotation.DPSRate;
import com.thorinchen.rate.limiter.core.converter.IRateConverter;
import org.apache.commons.lang3.StringUtils;


public final class DPSRateBean extends RateLimitBean {

    private IRateConverter converter;

    public DPSRateBean(DPSRate dpsRate, IRateConverter converter, RateLimitBean father) {
        setKey(StringUtils.isEmpty(dpsRate.key()) ? father.getKey() : dpsRate.key());
        setRate(dpsRate.rate() > 0 ? dpsRate.rate() : father.getRate());
        setStrategy(dpsRate.strategy());
        setConverter(converter);
    }

    public IRateConverter getConverter() {
        return converter;
    }

    public void setConverter(IRateConverter converter) {
        this.converter = converter;
    }
}