package com.adus.predictivecache.framework.components.interception;

public class FrameworkInitiatedThreadFlag {
    private static final ThreadLocal<Boolean> isFrameWorkInitiatedThread = new ThreadLocal<>();

    public static void set() {
        isFrameWorkInitiatedThread.set(true);
    }

    public static boolean isSet() {
        Boolean flag = isFrameWorkInitiatedThread.get();
        return flag == null ? false : flag;
    }
}
