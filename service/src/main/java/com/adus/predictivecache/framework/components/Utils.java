package com.adus.predictivecache.framework.components;

public class Utils {

    private Utils() {
        // utility class
    }

    public static String prepareCacheKey(String userId, String resourceId) {
        return userId + "::" + resourceId;
    }
}
