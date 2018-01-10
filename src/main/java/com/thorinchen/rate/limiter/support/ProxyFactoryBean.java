
package com.thorinchen.rate.limiter.support;

import com.thorinchen.rate.limiter.proxy.CglibProxy;
import net.sf.cglib.proxy.MethodInterceptor;
import org.springframework.beans.factory.FactoryBean;


public class ProxyFactoryBean implements FactoryBean {

    private Class<?> targetType;

    private MethodInterceptor interceptor;

    @Override
    public Object getObject() throws Exception {
        return CglibProxy.createProxy(targetType,interceptor);
    }

    @Override
    public Class<?> getObjectType() {
        return targetType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public Class<?> getTargetType() {
        return targetType;
    }

    public void setTargetType(Class<?> targetType) {
        this.targetType = targetType;
    }

    public MethodInterceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(MethodInterceptor interceptor) {
        this.interceptor = interceptor;
    }
}