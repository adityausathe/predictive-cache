package com.adus.predictivecache.framework.components;

import lombok.Data;
import org.aspectj.lang.JoinPoint;

import java.lang.reflect.Method;

@Data
public class ResourceElement {
    private final String className;
    private final String methodName;

    public static ResourceElement from(JoinPoint joinPoint) {
        return new ResourceElement(joinPoint.getSignature().getDeclaringType().getName(), joinPoint.getSignature().getName());
    }

    public static ResourceElement of(Class<?> beanClass, Method method) {
        return new ResourceElement(beanClass.getName(), method.getName());
    }
}
