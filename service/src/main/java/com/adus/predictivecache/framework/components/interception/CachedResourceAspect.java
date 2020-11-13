package com.adus.predictivecache.framework.components.interception;

import com.adus.predictivecache.framework.components.CachedResourceAccessHandler;
import com.adus.predictivecache.framework.components.CachedResultRetriever;
import com.adus.predictivecache.framework.components.ResourceElement;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CachedResourceAspect {

    @Around("@annotation(com.adus.predictivecache.framework.config.PredictivelyCachedResource)")
    public Object cachedResourceAccess(ProceedingJoinPoint joinPoint) throws Throwable {

        if (FrameworkInitiatedThreadFlag.isSet()) {
            return joinPoint.proceed();
        }

        ResourceElement resourceElement = ResourceElement.from(joinPoint);

        CachedResourceAccessHandler cachedResourceAccessHandler = getBean("cachedResourceAccessHandler", CachedResourceAccessHandler.class);
        cachedResourceAccessHandler.handle(resourceElement);

        CachedResultRetriever cachedResultRetriever = getBean("cachedResultRetriever", CachedResultRetriever.class);
        Object cachedResult = cachedResultRetriever.retrieve(resourceElement);

        if (cachedResult != null) {
            System.out.println(Thread.currentThread() + "-----returning cached result------" + cachedResult);
            return cachedResult;
        }
        return joinPoint.proceed();
    }

    private <T> T getBean(String beanName, Class<T> beanClass) {
        return SpringApplicationContextHolder.getApplicationContext().getBean(beanName, beanClass);
    }
}
