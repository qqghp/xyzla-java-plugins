package com.xyzla.common.context;

import java.util.Map;

public class DriverContext {

    private static ThreadLocal<Map<String, String>> scopeLocal = new ThreadLocal<Map<String, String>>();

    private static ThreadLocal<Map<String, String>> headerLocal = new ThreadLocal<Map<String, String>>();


    public static Map<String, String> getScope() {
        return scopeLocal.get();
    }

    public static void setScope(Map<String, String> scope) {
        scopeLocal.set(scope);
    }

    public static Map<String, String> getHeader() {
        return headerLocal.get();
    }

    public static void setHeader(Map<String, String> scope) {
        headerLocal.set(scope);
    }


    public static void clear() {
        scopeLocal.remove();
        headerLocal.remove();
    }
}
