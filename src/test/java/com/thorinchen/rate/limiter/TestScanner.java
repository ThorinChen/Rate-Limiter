
package com.thorinchen.rate.limiter;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;


public class TestScanner extends ClassPathBeanDefinitionScanner{

    private BeanDefinitionRegistry registry;

    private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();


    public TestScanner(BeanDefinitionRegistry registry) {
        super(registry,false);
        this.registry = registry;
        addIncludeFilter(new AnnotationTypeFilter(Bean.class));
    }

    protected Set<BeanDefinitionHolder> doScan(String... pkg) {
        Set<BeanDefinitionHolder> holders = super.doScan(pkg);

        for (BeanDefinitionHolder holder : holders) {
            System.out.println(holder);
            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) holder.getBeanDefinition();
//            beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }
        return holders;
    }
}