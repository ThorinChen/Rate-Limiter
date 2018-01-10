
package com.thorinchen.rate.limiter.core;

import com.google.common.util.concurrent.RateLimiter;

import java.util.HashSet;
import java.util.Set;


public class LimiterHolder {

    private RateLimiter rateLimiter;

    private LimitHolderDescriptor effectiveDescriptor;

    private Set<LimitHolderDescriptor> descriptors = new HashSet<>();

    public static LimiterHolder createNew(LimitHolderDescriptor descriptor) {
        return new LimiterHolder(descriptor);
    }

    public void addDescriptor(LimitHolderDescriptor descriptor) {

        double existsRate = effectiveDescriptor.getRate();
        double newRate = descriptor.getRate();

        if (existsRate == newRate) {
            getDescriptors().add(descriptor);
            return;
        }

        RateLimitStrategy existsStrategy = effectiveDescriptor.getStrategy();
        RateLimitStrategy newStrategy = descriptor.getStrategy();

        if (existsStrategy == RateLimitStrategy.THROW_EXCEPTION_WHEN_DIFFERENT || descriptor.getStrategy() == RateLimitStrategy.THROW_EXCEPTION_WHEN_DIFFERENT) {
            throw new IllegalStateException("different rate found![" + effectiveDescriptor + " vs " + descriptor + "]");
        }

        if (existsStrategy == RateLimitStrategy.REPLEASE && newStrategy == RateLimitStrategy.REPLEASE) {
            initNew(descriptor);
            return;
        }

        if (existsStrategy == RateLimitStrategy.USE_MIN && newStrategy == RateLimitStrategy.USE_MIN) {
            if (existsRate < newRate) {
                getDescriptors().add(descriptor);
                return;
            }
            initNew(descriptor);
            return;
        }

        if (existsStrategy == RateLimitStrategy.USE_EXISTS && newStrategy == RateLimitStrategy.USE_EXISTS) {
            getDescriptors().add(descriptor);
            return;
        }

        throw new IllegalStateException("incompatible rate found![" + effectiveDescriptor + " vs " + descriptor + "]");
    }

    private LimiterHolder(LimitHolderDescriptor descriptor) {
        initNew(descriptor);
    }

    private void initNew(LimitHolderDescriptor descriptor) {
        this.effectiveDescriptor = descriptor;
        this.rateLimiter = RateLimiter.create(descriptor.getRate());
        this.descriptors.add(descriptor);
    }

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public void setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    public LimitHolderDescriptor getEffectiveDescriptor() {
        return effectiveDescriptor;
    }

    public void setEffectiveDescriptor(LimitHolderDescriptor effectiveDescriptor) {
        this.effectiveDescriptor = effectiveDescriptor;
    }

    public Set<LimitHolderDescriptor> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(Set<LimitHolderDescriptor> descriptors) {
        this.descriptors = descriptors;
    }
}