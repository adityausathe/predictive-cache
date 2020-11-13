package com.adus.predictivecache.framework.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrendRepository extends MongoRepository<UserTrend, String> {
    UserTrend findByUserId(String userId);
}
