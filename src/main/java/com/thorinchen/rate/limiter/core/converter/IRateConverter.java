
package com.thorinchen.rate.limiter.core.converter;


public interface IRateConverter {

    int acquire(Object[] args);
}
