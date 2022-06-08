package com.xyzla.common.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
