package com.adus.predictivecache.framework.components;

import com.adus.predictivecache.framework.components.cache.ResourceResultCache;
import com.adus.predictivecache.framework.components.interception.FrameworkInitiatedThreadFlag;
import com.adus.predictivecache.framework.metrics.MetricRegistry;
import org.springframework.beans.factory.config.MethodInvokingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

import static com.adus.predictivecache.framework.components.Utils.prepareCacheKey;

@Component
public class ResourceExecutionAndCachingService {
    private final TaskExecutor taskExecutor;
    private final ApplicationContext applicationContext;
    private final ResourceResultCache resourceResultCache;

    public ResourceExecutionAndCachingService(TaskExecutor taskExecutor,
                                              ApplicationContext applicationContext,
                                              ResourceResultCache resourceResultCache) {
        this.taskExecutor = taskExecutor;
        this.applicationContext = applicationContext;
        this.resourceResultCache = resourceResultCache;
    }

    public void invokeResourceAndCacheResult(String userId, String resourceId, ResourceElement resourceElement) {
        taskExecutor.execute(() -> {

            FrameworkInitiatedThreadFlag.set();

            Object result = invokeMethod(resourceElement);
            System.out.println(Thread.currentThread().getName() + " --InvokedResult-- " + result);

            resourceResultCache.put(prepareCacheKey(userId, resourceId), result);
            MetricRegistry.recordCachedResultComputation(userId);
        });
    }

    private Object invokeMethod(ResourceElement resourceElement) {

        String targetClass = resourceElement.getClassName();
        String targetMethod = resourceElement.getMethodName();

        Object result = null;
        MethodInvokingBean methodInvokingBean = new MethodInvokingBean();

        try {
            Object bean = applicationContext.getBean(Class.forName(targetClass));
            methodInvokingBean.setTargetObject(bean);
            methodInvokingBean.setTargetMethod(targetMethod);
            methodInvokingBean.prepare();
            result = methodInvokingBean.invoke();
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }
}
