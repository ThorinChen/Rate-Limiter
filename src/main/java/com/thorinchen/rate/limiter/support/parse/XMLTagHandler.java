
package com.thorinchen.rate.limiter.support.parse;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


public class XMLTagHandler extends NamespaceHandlerSupport {

    @Override public void init() {
        registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanParser());
    }
}