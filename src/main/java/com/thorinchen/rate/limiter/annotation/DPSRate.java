package com.thorinchen.rate.limiter.annotation;

import com.thorinchen.rate.limiter.common.Rates;
import com.thorinchen.rate.limiter.core.RateLimitStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DPSRate {
    /**
     * 如果不指定，则默认为所加类的名称，方法的参数不指定则默认继承这个key
     */
    String key() default "";

    /**
     * 方法的参数不指定则默认继承这个rate
     *
     * @see
     */
    double rate() default Rates.RATE_USING_SUPER;

    /**
     * 如果相同的key的ratelimit已存在则采用什么策略
     */
    RateLimitStrategy strategy() default RateLimitStrategy.USE_MIN;
}