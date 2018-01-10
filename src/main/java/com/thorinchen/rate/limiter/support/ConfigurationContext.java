
package com.thorinchen.rate.limiter.support;

import com.google.common.base.Preconditions;
import com.thorinchen.rate.limiter.annotation.RateLimit;
import com.thorinchen.rate.limiter.RateLimitContext;
import com.thorinchen.rate.limiter.RateLimitRuntime;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;


public class ConfigurationContext implements BeanDefinitionRegistryPostProcessor, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationContext.class);

    private RateLimitContext rateLimitContext = new RateLimitContext();

    private RateLimitRuntime rateLimitRuntime = new RateLimitRuntime(rateLimitContext);
    /**
     * scan packages eg: com.xxx,org.xxx
     */
    private String scanPackages;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        AnnotationBeanScanner scanner = new AnnotationBeanScanner(beanDefinitionRegistry, RateLimit.class) {
            @Override
            void onClassFound(GenericBeanDefinition beanDefinition, BeanDefinitionRegistry registry) {

                Class<?> targetClass = beanDefinition.getBeanClass();
                rateLimitContext.addRateLimiters(targetClass);
                beanDefinition.setBeanClass(ProxyFactoryBean.class);
                beanDefinition.getPropertyValues().add("targetType", targetClass);
                beanDefinition.getPropertyValues().add("interceptor", rateLimitRuntime);

            }
        };

        scanner.scan(org.springframework.util.StringUtils.tokenizeToStringArray(this.scanPackages.trim(), ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        // do nothing
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkState(StringUtils.isNotBlank(scanPackages), "scanPackage can't be empty");
    }

    public String getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String scanPackages) {
        this.scanPackages = scanPackages;
    }
}