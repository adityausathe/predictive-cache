package com.adus.predictivecache.framework.components;

import com.adus.predictivecache.framework.components.interception.SpringApplicationContextHolder;
import com.adus.predictivecache.framework.config.PredictivelyCachedResource;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@DependsOn("springApplicationContextHolder")
@Component
public class CachableResourceRegistrar {

    private BidiMap<ResourceElement, String> registry;

    public CachableResourceRegistrar() {
        this.registry = new DualHashBidiMap<>();
        initRegistry(SpringApplicationContextHolder.getApplicationContext());
    }

    private void initRegistry(ApplicationContext applicationContext) {
        String[] allBeans = applicationContext.getBeanDefinitionNames();
        Arrays.stream(allBeans).forEach(bean -> {
            if (bean.equalsIgnoreCase(CachableResourceRegistrar.class.getSimpleName())) {
                return;
            }
            Class<?> beanClass = applicationContext.getBean(bean).getClass().getSuperclass();
            Method[] methods = beanClass.getDeclaredMethods();
            Map<ResourceElement, String> resourcesInThisClass = Arrays.stream(methods)
                    .filter(method -> method.isAnnotationPresent(PredictivelyCachedResource.class))
                    .collect(Collectors.toMap(
                            method -> ResourceElement.of(beanClass, method),
                            method -> determineResourceId(beanClass, method))
                    );

            this.registry.putAll(resourcesInThisClass);
        });
    }

    private String determineResourceId(Class<?> beanClass, Method method) {
        String idFromAnnotation = method.getAnnotation(PredictivelyCachedResource.class).id();
        return idFromAnnotation.equals(PredictivelyCachedResource.DEFAULT_RESOURCE_ID)
                ? beanClass.getName() + "_" + method.getName() : idFromAnnotation;
    }

    public String getResourceId(ResourceElement resourceElement) {
        return registry.get(resourceElement);
    }

    public ResourceElement getResourceElement(String resourceId) {
        return registry.getKey(resourceId);
    }
}
