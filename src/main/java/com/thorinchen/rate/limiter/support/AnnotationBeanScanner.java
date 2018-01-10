
package com.thorinchen.rate.limiter.support;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Set;


public abstract class AnnotationBeanScanner extends ClassPathBeanDefinitionScanner {

    private BeanDefinitionRegistry registry;

    @SafeVarargs
    public AnnotationBeanScanner(BeanDefinitionRegistry registry, Class<? extends Annotation>... annotations) {
        super(registry, false);
        this.registry = registry;
        addFilters(annotations);
    }

    protected Set<BeanDefinitionHolder> doScan(String... pkg) {
        Set<BeanDefinitionHolder> holders = super.doScan(pkg);

        for (BeanDefinitionHolder holder : holders) {
            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = beanDefinition.getBeanClassName();
            try {
                beanDefinition.setBeanClass(Class.forName(beanClassName));
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("invalid class:" + beanClassName);
            }
            onClassFound(beanDefinition, registry);
            beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }
        return holders;
    }

    private void addFilters(Class<? extends Annotation>[] annotations) {
        for (Class<? extends Annotation> annotation : annotations) {
            addIncludeFilter(new AnnotationTypeFilter(annotation));
        }
    }

    abstract void onClassFound(GenericBeanDefinition beanDefinition, BeanDefinitionRegistry registry);
}