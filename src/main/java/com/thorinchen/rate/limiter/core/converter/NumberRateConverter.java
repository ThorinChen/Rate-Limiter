
package com.thorinchen.rate.limiter.core.converter;

import org.apache.commons.beanutils.ConvertUtils;


public class NumberRateConverter extends IndexArgRateConverter {

    public NumberRateConverter(int argIndex) {
        super(argIndex);
    }

    @Override
    public int acquire(Object[] args) {
        return (int) ConvertUtils.convert(args[argIndex], int.class);
    }
}