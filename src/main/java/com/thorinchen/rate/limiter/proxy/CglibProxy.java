
package com.thorinchen.rate.limiter.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;


public class CglibProxy {

    public static Object createProxy(Class<?> target, MethodInterceptor interceptor) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target);
        enhancer.setCallback(interceptor);
        return enhancer.create();
    }

}