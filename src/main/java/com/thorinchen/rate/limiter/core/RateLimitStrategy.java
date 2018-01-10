
package com.thorinchen.rate.limiter.core;


public enum RateLimitStrategy {

    USE_MIN,
    THROW_EXCEPTION_WHEN_DIFFERENT,
    REPLEASE,
    USE_EXISTS
    ;

}