
package com.thorinchen.rate.limiter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;


public class AppContext implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {

    private TestScanner scanner;
    private String name;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        GenericApplicationContext app = (GenericApplicationContext) applicationContext;
        scanner = new TestScanner(app);
        System.out.println("setapp");
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        System.out.println("post:" + name);
        scanner.scan("com");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}