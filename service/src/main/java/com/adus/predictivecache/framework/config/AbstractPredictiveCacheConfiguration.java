package com.adus.predictivecache.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class AbstractPredictiveCacheConfiguration {

    @Bean
    public abstract UserIdentifierProvider userIdentifierProvider();

    @Bean
    public abstract RequestTimeStampProvider requestTimeStampProvider();
}
