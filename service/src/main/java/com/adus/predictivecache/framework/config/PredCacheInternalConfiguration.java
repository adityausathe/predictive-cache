package com.adus.predictivecache.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.adus.predictivecache.framework", mongoTemplateRef = "predCacheMongoTemplate")
public class PredCacheInternalConfiguration {
    @Autowired
    private PredCacheProperties predCacheProperties;

    private MongoDbFactory mongoDbFactory() {
        return new SimpleMongoClientDbFactory(predCacheProperties.getMongoDbUri());
    }

    @Bean(name = "predCacheMongoTemplate")
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }
}
