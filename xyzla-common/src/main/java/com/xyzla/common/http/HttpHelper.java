package com.xyzla.common.http;

import com.xyzla.common.exception.ConnTimeOutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class HttpHelper {

    private static final Logger logger = LoggerFactory.getLogger(HttpHelper.class);
    public static String REQUEST_ERROR = "request error";

    private int period = 1; // 间隔时间，以分为单位
    private static int times = 2; // 间隔倍数
    private static int num = 3; // 调度次数
    protected int capcity = 0;

    private String str; // 描述信息

    private Object lock = new Object();

    private SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public HttpHelper(String str) {
        this.str = str;
    }

    public String connect(String url, String body, boolean isSSL, Map<String, String> headers) {
        return connect(url, body, isSSL, true, headers);
    }

    public String connect(String url, String body, boolean isSSL, boolean isPost, Map<String, String> headers) {
        String result = "";
        try {
            if (capcity == num) {
                if (logger.isDebugEnabled()) {
                    logger.debug("请求地址 [ {} ] 数据 [ {} ] 方式 [ {} ] 次数 [ {} ]", url, null, (isPost ? "POST" : "GET"), num);
                }
                return REQUEST_ERROR;
            }

            if (capcity++ != 0) {
                taskWait();
            }

            if (logger.isDebugEnabled()) {
                logger.debug("{} :请求的url: [ {} ] ，连接参数为：[ {} ]，开始连接", str, url, null);
            }
            if (isPost) {
                result = HttpConnector.requestPost(url, body, isSSL, headers);
            } else {
                result = HttpConnector.requestGet(url, body, isSSL, headers);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("{} :请求的url: [ {} ] ，连接参数为：[ {} ] 连接成功，该连接执行结束", str, url, null);
            }
        } catch (ConnTimeOutException e) {
            logger.error(e.getStepDesc(), e);
            logger.debug("{} :request url: [ {} ] ，paramaters: [ {} ] 由于网络异常导致连接中断，下次重新连接时间间隔为：[ {} ] 分钟，时间为: {}", str, url, null, period, formate.format(new Date(System.currentTimeMillis() + period * 60 * 1000)));
            nextPeriod();

            result = this.connect(url, body, isSSL, isPost, headers);
        } catch (Exception e) {
            logger.error("错误", e);
            logger.debug("{} request url: {} ，paramater: {} 由于代码异常导致失败，该异常不能重新执行，请检查代码，该连接执行结束", str, url, null);
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
