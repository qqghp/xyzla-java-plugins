package com.xyzla.common.api;

import com.xyzla.common.api.response.ResBean;
import com.xyzla.common.exception.ApiAccessException;
import com.xyzla.common.function.Consumer;
import com.xyzla.common.function.Handler;
import com.xyzla.common.function.Supplier;
import com.xyzla.common.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceException;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseApi {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public HttpServletRequest getRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) ra).getRequest();
    }

    public HttpServletResponse getResponse() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) ra).getResponse();
    }

    protected Map<String, String[]> getRequestParam() {
        return getRequest().getParameterMap();
    }

    protected String getRequestParamAsString() {
        return JacksonUtil.toJson(getRequestParam());
    }

    protected ResBean response(Supplier<Object> supplier) {
        HttpServletRequest request = getRequest();
        String uri = request.getRequestURI();
        String apiVersion = request.getHeader("apiVer");
        String method = request.getMethod();

        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            Object result = supplier.get();
            stopWatch.stop();
            logger.info("method {} uri {} apiVersion {} cost {} millis",
                    uri,
                    method,
                    apiVersion,
                    stopWatch.getTotalTimeMillis());
            if (result == null) {
                return ResBean.ofSuccess();
            } else if (result instanceof ResBean) {
                return (ResBean) result;
            } else {
                return ResBean.ofSuccess(result);
            }
        } catch (ApiAccessException e) {
            logger.error(
                    "method {} uri {} apiVersion {} toString {}",
                    method,
                    uri,
                    apiVersion,
                    e.toString());
            return ResBean.ofServerError(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(
                    "{}",
                    getRequestParamAsString(),
                    e);
            logger.error("Handle Post other exception, {}, {}, {}", uri, apiVersion, e);
            return ResBean.ofServerError("internal error");
        }
    }

    protected ResBean response(Consumer<Map<String, Object>> consumer) {
        return response(
                () -> {
                    Map<String, Object> resultMap = new HashMap<>();
                    consumer.accept(resultMap);
                    return resultMap;
                });
    }

    protected ResBean doRequest(Handler handler) {
        return response(
                () -> {
                    handler.invoke();
                    return null;
                });
    }

    public ResBean doPost(Object body, Supplier<Object> resultFuc) {
        try {
            logger.debug("doPost {}", body);
            Object result = resultFuc.get();
            if (result == null) {
                return ResBean.ofSuccess();
            } else if (result instanceof ResBean) {
                return (ResBean) result;
            } else {
                return ResBean.ofSuccess(result);
            }

        } catch (ApiAccessException e) {
            logger.error(
                    "{}",
                    getRequestParamAsString(),
                    e);
            return ResBean.ofServerError(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(
                    "{}",
                    getRequestParamAsString(),
                    e);
            return ResBean.ofServerError("internal error");
        }
    }

    protected ResBean doRequest(Supplier<Object> handle) {
        return this.doPost(null, () -> handle.get());
    }

}
