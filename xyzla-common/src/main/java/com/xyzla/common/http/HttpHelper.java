package com.xyzla.common.http;

import com.xyzla.common.exception.ConnTimeOutException;
import com.xyzla.common.util.JacksonUtil;
import com.xyzla.common.util.ZonedUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class HttpHelper {
    private static final Logger logger = LoggerFactory.getLogger(HttpHelper.class);

    public static String REQUEST_ERROR = "request error";

    private int period = 1; // 间隔时间，以分为单位
    private static int times = 2; // 间隔倍数
    private static int num = 3; // 调度次数
    protected int capcity = 0;

    private String desc; // 描述信息

    private Object lock = new Object();

    public HttpHelper(String desc) {
        this.desc = desc;
    }

    public <T> String connect(String url, T requestBody, HttpMethod httpMethod, Map<String, String> headers, List<String> uriVariables) {
        String _url = null;
        if (uriVariables != null && !uriVariables.isEmpty()) {
            _url = UrlVariables.replace(url, uriVariables);
        } else {
            _url = url;
        }

        return connect(_url, requestBody, httpMethod, headers);
    }

    public <T> String connect(String url, T requestBody, HttpMethod httpMethod, Map<String, String> headers) {
        String result = "";


        String body = null;
        if (requestBody instanceof String) {
            body = (String) requestBody;
        } else {
            body = JacksonUtil.toJson(requestBody);
        }

        try {
            if (capcity == num) {
                if (logger.isDebugEnabled()) {
                    logger.debug("{} {} request num {}", httpMethod.name(), url, num);
                }
                return REQUEST_ERROR;
            }

            if (capcity++ != 0) {
                taskWait();
            }

            if (logger.isDebugEnabled()) {
                logger.debug("{} :请求的url: [ {} ] ，连接参数为：[ {} ]，开始连接", desc, url, null);
            }

            if (httpMethod.matches("POST")) {
                result = HttpConnector.requestPost(url, body, headers);
            } else if (httpMethod.matches("GET")) {
                result = HttpConnector.requestGet(url, body, headers);
            } else {
                throw new ConnTimeOutException("Unsupport HttpMethod");
            }

            if (logger.isDebugEnabled()) {
                logger.debug("{} :请求的url: [ {} ] ，连接参数为：[ {} ] 连接成功，该连接执行结束", desc, url, null);
            }
        } catch (ConnTimeOutException e) {
            logger.error(e.getStepDesc(), e);
            logger.debug("{} :request url: [ {} ] ，paramaters: [ {} ] 由于网络异常导致连接中断，下次重新连接时间间隔为：[ {} ] 分钟，时间为: {}", desc, url, null, period, LocalDateTime.now().plusSeconds(period * 60).format(ZonedUtil.DTF_DTSS));
            nextPeriod();

            result = this.connect(url, requestBody, httpMethod, headers);
        } catch (Exception e) {
            logger.error("错误", e);
            logger.debug("{} request url: {} ，paramater: {} 由于代码异常导致失败，该异常不能重新执行，请检查代码，该连接执行结束", desc, url, null);
        }

        return result;
    }

    private void nextPeriod() {
        period *= times;
    }

    private void taskWait() {
        synchronized (lock) {
            try {
                lock.wait(period * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
