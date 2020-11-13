package com.adus.predictivecache.framework.components;

import com.adus.predictivecache.framework.components.cache.ResourceResultCache;
import com.adus.predictivecache.framework.config.UserIdentifierProvider;
import com.adus.predictivecache.framework.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static com.adus.predictivecache.framework.components.Utils.prepareCacheKey;

@Component
public class CachedResultRetriever {

    private final CachableResourceRegistrar cachableResourceRegistrar;
    private final UserIdentifierProvider userIdentifierProvider;
    private final ResourceResultCache resourceResultCache;

    @Autowired
    public CachedResultRetriever(@Lazy CachableResourceRegistrar cachableResourceRegistrar,
                                 UserIdentifierProvider userIdentifierProvider,
                                 ResourceResultCache resourceResultCache) {
        this.cachableResourceRegistrar = cachableResourceRegistrar;
        this.userIdentifierProvider = userIdentifierProvider;
        this.resourceResultCache = resourceResultCache;
    }

    public Object retrieve(ResourceElement resourceElement) {
        String userId = userIdentifierProvider.getUserId();
        String resourceId = cachableResourceRegistrar.getResourceId(resourceElement);
        Object cachedResult = resourceResultCache.remove(prepareCacheKey(userId, resourceId));
        if (cachedResult != null) {
            MetricRegistry.recordHit(userId);
        }
        return cachedResult;
    }

}
