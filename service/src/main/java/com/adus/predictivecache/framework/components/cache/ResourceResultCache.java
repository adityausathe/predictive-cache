package com.adus.predictivecache.framework.components.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ResourceResultCache {

    private static final Map<String, Object> CACHE = new ConcurrentHashMap<>();

    public void put(String key, Object value) {
        CACHE.put(key, value);
    }

    public Object get(String key) {
        return CACHE.get(key);
    }

    public Object remove(String key) {
        return CACHE.remove(key);
    }
}
