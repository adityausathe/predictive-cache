package com.adus.predictivecache.client.config;

import com.adus.predictivecache.framework.config.AbstractPredictiveCacheConfiguration;
import com.adus.predictivecache.framework.config.RequestTimeStampProvider;
import com.adus.predictivecache.framework.config.UserIdentifierProvider;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PredictiveCacheClientConfiguration extends AbstractPredictiveCacheConfiguration {
    @Override
    public UserIdentifierProvider userIdentifierProvider() {
        return ThreadContextHolder::getUserId;
    }

    @Override
    public RequestTimeStampProvider requestTimeStampProvider() {
        return ThreadContextHolder::getRequestTimeStamp;
    }
}
