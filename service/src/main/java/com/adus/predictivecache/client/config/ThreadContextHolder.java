package com.adus.predictivecache.client.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ThreadContextHolder {
    private static ThreadLocal<String> threadLocalUserId = new ThreadLocal<>();

    private static ThreadLocal<LocalDateTime> threadLocalTimeStamp = new ThreadLocal<>();

    public static String getUserId() {
        return threadLocalUserId.get();
    }

    public static void setUserId(String userId) {
        threadLocalUserId.set(userId);
    }

    public static LocalDateTime getRequestTimeStamp() {
        return threadLocalTimeStamp.get();
    }

    public static void setRequestTimeStamp(String timeStampStr) {
        LocalDateTime timeStamp;
        if (timeStampStr != null) {
            timeStamp = LocalDateTime.parse(timeStampStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            timeStamp = LocalDateTime.now();
        }
        threadLocalTimeStamp.set(timeStamp);
    }
}
