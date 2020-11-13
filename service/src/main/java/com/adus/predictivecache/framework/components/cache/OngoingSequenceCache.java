package com.adus.predictivecache.framework.components.cache;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OngoingSequenceCache {

    private static final Map<String, List<String>> CACHE = new ConcurrentHashMap<>();

    public void put(String userId, List<String> sequenceItems) {
        CACHE.put(userId, new ArrayList<>(sequenceItems));
    }

    public List<String> get(String userId) {
        List<String> sequence = CACHE.get(userId);
        return sequence == null ? null : new ArrayList<>(sequence);
    }

    public void remove(String userId) {
        CACHE.remove(userId);
    }
}
