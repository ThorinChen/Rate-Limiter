
package com.thorinchen.rate.limiter;

import com.thorinchen.rate.limiter.core.acquire.IRLimitAcquirable;
import com.thorinchen.rate.limiter.proxy.CglibProxy;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collection;


public class RateLimitRuntime implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitRuntime.class);

    private RateLimitContext context;

    public RateLimitRuntime(RateLimitContext context) {
        this.context = context;
    }

    private void methodExecuteAcquire(Method method, Object[] parameters) {
        String methodKey = method.toGenericString();
        Collection<IRLimitAcquirable> acquireCollections = context.getAcquireByKey(methodKey);
        if (acquireCollections != null && acquireCollections.size() > 0) {
            for (IRLimitAcquirable acquirable : acquireCollections) {
                acquirable.acquire(parameters);
            }
        }
    }

    @Override
    public Object intercept(Object target, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        methodExecuteAcquire(method, objects);
        logger.debug("running method:{}", method);
        return methodProxy.invokeSuper(target, objects);
    }

    Class<?> createProxyClass(Class<?> target) {
        return CglibProxy.createProxy(target, this).getClass();
    }

    public <T> T create(Class<T> target) {
        return target.cast(CglibProxy.createProxy(target, this));
    }
}