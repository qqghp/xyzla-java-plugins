package com.xyzla.common.context;


import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class XyzlaServletContext {

    private static ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal<>();

    private static ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal<>();

    public static void setServletContext(ServletRequest req, ServletResponse res) {
        requestLocal.set((HttpServletRequest) req);
        responseLocal.set((HttpServletResponse) res);
    }

    public static HttpServletRequest getRequest() {
        return requestLocal.get();
    }

    public static HttpServletResponse getResponse() {
        return responseLocal.get();
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static ServletContext getApplication() {
        return getSession().getServletContext();
    }

    public static void clear() {
        requestLocal.remove();
        responseLocal.remove();
    }
}
