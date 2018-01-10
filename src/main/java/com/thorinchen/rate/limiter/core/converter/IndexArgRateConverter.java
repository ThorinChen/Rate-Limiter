
package com.thorinchen.rate.limiter.core.converter;


public abstract class IndexArgRateConverter implements IRateConverter {

    protected int argIndex;

    public IndexArgRateConverter(int argIndex) {
        this.argIndex = argIndex;
    }

    public int getArgIndex() {
        return argIndex;
    }
}