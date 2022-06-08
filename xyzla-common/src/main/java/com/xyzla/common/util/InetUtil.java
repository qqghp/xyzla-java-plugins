package com.xyzla.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetUtil {

    private static final Logger logger = LoggerFactory.getLogger(InetUtil.class);

    public static String getHostName() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            String ip = ia.getHostAddress(); // 获取计算机IP
            String host = ia.getHostName(); // 获取计算机主机名
            logger.info("HostName: {}, IP: {}", host, ip);
            return host;
        } catch (UnknownHostException e) {
            logger.error("Query HostName Exception. {}", e);
        }
        return "UnknownHost";
    }
}
