package com.adus.predictivecache.framework.metrics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MetricRegistry {
    private static final Map<String, Integer> REQUEST_STORE = new ConcurrentHashMap<>();
    private static final Map<String, Integer> HIT_STORE = new ConcurrentHashMap<>();
    private static final Map<String, Integer> CACHED_COMP_STORE = new ConcurrentHashMap<>();

    public static void recordRequest(String userId) {
        incrementCount(userId, REQUEST_STORE);
    }

    public static void recordHit(String userId) {
        incrementCount(userId, HIT_STORE);
    }

    public static void recordCachedResultComputation(String userId) {
        incrementCount(userId, CACHED_COMP_STORE);
    }

    private static void incrementCount(String userId, Map<String, Integer> store) {
        store.merge(userId, 1, Integer::sum);
    }

    public static String getRequestReport() {
        return generateReport(REQUEST_STORE);
    }

    public static String getCachedResultComputationReport() {
        return generateReport(CACHED_COMP_STORE);
    }

    public static String getHitReport() {
        return generateReport(HIT_STORE);
    }

    private static String generateReport(Map<String, Integer> store) {
        List<String> userIds = new ArrayList<>(store.keySet());
        userIds.sort(Comparator.naturalOrder());

        return userIds.stream()
                .map(userId -> userId + "," + store.get(userId))
                .collect(Collectors.joining("\n"));
    }

    public static void clear() {
        REQUEST_STORE.clear();
        HIT_STORE.clear();
        CACHED_COMP_STORE.clear();
    }
}
