
package com.thorinchen.rate.limiter.core.converter;

import java.lang.reflect.Array;


public class ArrayLengthRateConverter extends IndexArgRateConverter {

    public ArrayLengthRateConverter(int argIndex) {
        super(argIndex);
    }

    @Override
    public int acquire(Object[] args) {
        Object array = args[getArgIndex()];
        return Array.getLength(array);
    }
}