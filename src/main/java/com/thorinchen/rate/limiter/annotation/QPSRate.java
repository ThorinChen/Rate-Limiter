package com.thorinchen.rate.limiter.annotation;

import com.thorinchen.rate.limiter.common.Rates;
import com.thorinchen.rate.limiter.core.RateLimitStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QPSRate {
    /**
     * 如果不指定，则默认为所加方法的全称
     */
    String key() default "";

    /**
     * 方法不指定则默认继承所在类的rate
     *
     * @see RateLimit
     */
    double rate() default Rates.RATE_USING_SUPER;

    /**
     * 一次调用需要多少个rate 默认为1，及一次query
     */
    int acquire() default 1;

    /**
     * 如果相同的key的ratelimit已存在则采用什么策略
     */
    RateLimitStrategy strategy() default RateLimitStrategy.USE_MIN;
}