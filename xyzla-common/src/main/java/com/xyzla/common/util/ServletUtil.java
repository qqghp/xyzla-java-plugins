package com.xyzla.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class ServletUtil {
    private static final Logger logger = LoggerFactory.getLogger(ServletUtil.class);

    public static String getIpAddr(HttpServletRequest request) {
        String ip = "";
        try {
            request.getHeader("x-Original-Forwarded-For");//获取 真实IP


            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("x-forwarded-for");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }

            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
                    InetAddress inetAddress = null;
                    try {
                        inetAddress = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ip = inetAddress.getHostAddress();
                }
            }
            if (ip != null && ip.indexOf(",") != -1) {
                String[] ipWithMultiProxy = ip.split(",");
                for (int i = 0; i < ipWithMultiProxy.length; ++i) {
                    String eachIpSegement = ipWithMultiProxy[i];
                    if (!"unknown".equalsIgnoreCase(eachIpSegement)) {
                        ip = eachIpSegement;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return ip;
    }

    public static long getIpToLong(HttpServletRequest request) {
        return ipToLong(getIpAddr(request));
    }

    /**
     * ip地址转成long型数字<br>
     * 将IP地址转化成整数的方法如下：<br>
     * 1、通过String的split方法按.分隔得到4个长度的数组<br>
     * 2、通过左移位操作（<<）给每一段的数字加权，第一段的权为2的24次方，第二段的权为2的16次方，第三段的权为2的8次方，最后一段的权为1<br>
     *
     * @param strIp
     */
    public static long ipToLong(String strIp) {
        String[] ip = strIp.split("\\.");
        return (Long.parseLong(ip[0]) << 24)
                + (Long.parseLong(ip[1]) << 16)
                + (Long.parseLong(ip[2]) << 8)
                + Long.parseLong(ip[3]);
    }

    /**
     * 将十进制整数形式转换成127.0.0.1形式的ip地址<br>
     * 将整数形式的IP地址转化成字符串的方法如下：<br>
     * 1、将整数值进行右移位操作（>>>），右移24位，右移时高位补0，得到的数字即为第一段IP。<br>
     * 2、通过与操作符（&）将整数值的高8位设为0，再右移16位，得到的数字即为第二段IP。<br>
     * 3、通过与操作符吧整数值的高16位设为0，再右移8位，得到的数字即为第三段IP。<br>
     * 4、通过与操作符吧整数值的高24位设为0，得到的数字即为第四段IP。<br>
     *
     * @param longIp
     */
    public static String longToIP(long longIp) {
        StringBuffer sb = new StringBuffer("");
        //  直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        //  将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        //  将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        //  将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }
}
