package com.adus.predictivecache.framework.components;

import com.adus.predictivecache.framework.config.UserIdentifierProvider;
import com.adus.predictivecache.framework.metrics.MetricRegistry;
import com.adus.predictivecache.framework.services.AccessRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CachedResourceAccessHandler {

    private final AccessRecordService accessRecordService;
    private final CachableResourceRegistrar cachableResourceRegistrar;
    private final UserIdentifierProvider userIdentifierProvider;
    private final NextResourcePredictor nextResourcePredictor;
    private final ResourceExecutionAndCachingService resourceExecutionAndCachingService;

    @Autowired
    public CachedResourceAccessHandler(AccessRecordService accessRecordService,
                                       @Lazy CachableResourceRegistrar cachableResourceRegistrar,
                                       UserIdentifierProvider userIdentifierProvider,
                                       NextResourcePredictor nextResourcePredictor,
                                       ResourceExecutionAndCachingService resourceExecutionAndCachingService) {
        this.accessRecordService = accessRecordService;
        this.cachableResourceRegistrar = cachableResourceRegistrar;
        this.userIdentifierProvider = userIdentifierProvider;
        this.nextResourcePredictor = nextResourcePredictor;
        this.resourceExecutionAndCachingService = resourceExecutionAndCachingService;
    }

    public void handle(ResourceElement resourceElement) {
        String userId = userIdentifierProvider.getUserId();
        String currentResourceId = cachableResourceRegistrar.getResourceId(resourceElement);

        accessRecordService.recordAccess(userId, currentResourceId);
        MetricRegistry.recordRequest(userId);

        Optional<String> predictedResourceId = nextResourcePredictor.predict(userId, currentResourceId);
        predictedResourceId.ifPresent(nextResourceId ->
                resourceExecutionAndCachingService.invokeResourceAndCacheResult(userId, nextResourceId, cachableResourceRegistrar.getResourceElement(nextResourceId)));
    }

}
