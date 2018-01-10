
package com.thorinchen.rate.limiter.support.parse;

import com.google.common.base.Preconditions;
import com.thorinchen.rate.limiter.support.ConfigurationContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;


public class AnnotationDrivenBeanParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        Class<ConfigurationContext> configurationContextClazz = ConfigurationContext.class;
        RootBeanDefinition configContextBean = new RootBeanDefinition(configurationContextClazz);

        String packages = element.getAttribute("packages");
        Preconditions.checkState(StringUtils.isNotBlank(packages), "packages can't be empty");
        configContextBean.getPropertyValues().add("scanPackages", packages);

        parserContext.getRegistry().registerBeanDefinition(configurationContextClazz.getName(), configContextBean);
        return configContextBean;
    }
}