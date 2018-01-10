package com.thorinchen.rate.limiter;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.thorinchen.rate.limiter.annotation.DPSRate;
import com.thorinchen.rate.limiter.annotation.QPSRate;
import com.thorinchen.rate.limiter.annotation.RateLimit;
import com.thorinchen.rate.limiter.common.DPSRateBean;
import com.thorinchen.rate.limiter.common.QPSRateBean;
import com.thorinchen.rate.limiter.common.RateLimitBean;
import com.thorinchen.rate.limiter.core.LimitHolderDescriptor;
import com.thorinchen.rate.limiter.core.LimiterHolder;
import com.thorinchen.rate.limiter.core.acquire.DPSLimitAcquirable;
import com.thorinchen.rate.limiter.core.acquire.IRLimitAcquirable;
import com.thorinchen.rate.limiter.core.acquire.QPSLimitAcquirable;
import com.thorinchen.rate.limiter.core.converter.ArrayLengthRateConverter;
import com.thorinchen.rate.limiter.core.converter.NumberRateConverter;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class RateLimitContext {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitContext.class);
    private Map<String, LimiterHolder> limitKeyMapRLimiter = new HashMap<>(16);
    private Multimap<String, IRLimitAcquirable> acquirableMap = HashMultimap.create();


    public void addRateLimiters(Class<?> clazz) {
        RateLimit rateLimit = AnnotationUtils.findAnnotation(clazz, RateLimit.class);

        if (rateLimit != null) {
            Method[] methods = clazz.getDeclaredMethods();
            if (methods != null && methods.length > 0) {
                RateLimitBean classRateLimitBean = new RateLimitBean(rateLimit, clazz);

                RateLimitBean methodParentBean = classRateLimitBean.cloneOne();
                for (Method method : methods) {
                    searchMethod(methodParentBean, method);
                }
            }

        }
    }

    private void searchMethod(RateLimitBean methodParentBean, Method method) {
        QPSRate qpsRate = AnnotationUtils.findAnnotation(method, QPSRate.class);
        String methodKey = method.toGenericString();
        methodParentBean.setKey(methodKey);
        QPSLimitAcquirable qpsLimitAcquirable = null;
        QPSRateBean methodRateBean = null;
        if (qpsRate != null) {

            methodRateBean = new QPSRateBean(qpsRate, methodParentBean);
            LimitHolderDescriptor descriptor = new LimitHolderDescriptor(methodRateBean, methodKey);

            LimiterHolder holderByKey = getHolderByKey(descriptor.getLimitKey());
            if (holderByKey != null) {
                holderByKey.addDescriptor(descriptor);
            } else {
                holderByKey = LimiterHolder.createNew(descriptor);
                saveHolderByKey(descriptor.getLimitKey(), holderByKey);
            }

            qpsLimitAcquirable = new QPSLimitAcquirable(holderByKey.getRateLimiter(), methodRateBean.getAcquire());
            saveAcquire(methodKey, qpsLimitAcquirable);
        }

        RateLimitBean parameterParentBean = methodRateBean == null ? methodParentBean : methodRateBean;

        searchParameters(method, methodKey, parameterParentBean);
    }

    private void searchParameters(Method method, String methodKey, RateLimitBean parameterParentBean) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations.length > 0) {
            int parameterIndex = 0;
            for (Annotation[] parameterAnnotation : parameterAnnotations) {
                if (parameterAnnotation.length > 0) {
                    for (Annotation annotation : parameterAnnotation) {
                        if (annotation instanceof DPSRate) {
                            DPSRate dpsRate = (DPSRate) annotation;
                            Class<?> parameterType = parameterTypes[parameterIndex];
                            DPSRateBean parameterRateBean = null;
                            if (parameterType.isArray()) {
                                parameterRateBean = new DPSRateBean(dpsRate, new ArrayLengthRateConverter(parameterIndex), parameterParentBean);
                            } else {
                                try {
                                    ConvertUtils.convert(parameterTypes[parameterIndex], int.class);
                                    parameterRateBean = new DPSRateBean(dpsRate, new NumberRateConverter(parameterIndex), parameterParentBean);
                                } catch (Exception e) {
                                    logger.error("convert error", e);
                                    throw new IllegalArgumentException(
                                            "@DPSRate can only be marked with Numbers or Array, but actually is:[" + methodKey + "->" + parameterIndex + ":" + parameterType + "]");
                                }
                            }
                            LimitHolderDescriptor dpsDescriptor = new LimitHolderDescriptor(parameterRateBean, methodKey);
                            LimiterHolder holderByKey = getHolderByKey(dpsDescriptor.getLimitKey());
                            if (holderByKey != null) {
                                holderByKey.addDescriptor(dpsDescriptor);
                            } else {
                                holderByKey = LimiterHolder.createNew(dpsDescriptor);
                            }

                            DPSLimitAcquirable dpsLimitAcquirable = new DPSLimitAcquirable(holderByKey.getRateLimiter(), parameterRateBean.getConverter());
                            saveAcquire(methodKey, dpsLimitAcquirable);
                        }
                    }
                }
                parameterIndex++;
            }
        }
    }

    public Collection<IRLimitAcquirable> getAcquireByKey(String key) {
        return this.acquirableMap.get(key);
    }

    private void addIRLimitAcquirableRelation(Method method, IRLimitAcquirable acquirable) {
        this.acquirableMap.put(method.toGenericString(), acquirable);
    }

    private LimiterHolder getHolderByKey(String key) {
        return this.limitKeyMapRLimiter.get(key);
    }

    private void saveHolderByKey(String key, LimiterHolder holder) {
        this.limitKeyMapRLimiter.put(key, holder);
    }

    private void saveAcquire(String methodKey, IRLimitAcquirable acquirable) {
        this.acquirableMap.put(methodKey, acquirable);
    }
}