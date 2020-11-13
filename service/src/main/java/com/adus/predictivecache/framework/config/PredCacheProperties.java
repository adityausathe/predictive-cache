package com.adus.predictivecache.framework.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class PredCacheProperties {

    @Value("${com.adus.predictivecache.mongoDbUri}")
    private String mongoDbUri;
    @Value("${com.adus.predictivecache.analytics.folder}")
    private String analyticsFolder;
    @Value("${com.adus.predictivecache.trend-type}")
    private String trendType;
    @Value("${com.adus.predictivecache.trend-percentile:0.8}")
    private float trendPercentile;
}


