package com.adus.predictivecache.framework.services.matchers;

import com.adus.predictivecache.framework.config.PredCacheProperties;
import org.springframework.stereotype.Service;

@Service
public class MatchersFactory {
    private final PredCacheProperties predCacheProperties;
    private final DependentTrendMatcher dependentTrendMatcher;
    private final IndependentTrendMatcher independentTrendMatcher;

    public MatchersFactory(PredCacheProperties predCacheProperties,
                           DependentTrendMatcher dependentTrendMatcher,
                           IndependentTrendMatcher independentTrendMatcher) {
        this.predCacheProperties = predCacheProperties;
        this.dependentTrendMatcher = dependentTrendMatcher;
        this.independentTrendMatcher = independentTrendMatcher;
    }

    public TrendMatcher get() {
        if (predCacheProperties.getTrendType().equalsIgnoreCase("Dependent")) {
            return dependentTrendMatcher;
        } else {
            return independentTrendMatcher;
        }
    }
}
