package com.xyzla.common.http;


import com.xyzla.common.exception.ConnTimeOutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;


public class HttpConnector {

    public static final Logger logger = LoggerFactory.getLogger(HttpConnector.class);

    public static String requestPost(String url, String param, Map<String, String> headers) throws Exception {
        return connect(url, param, HttpMethod.POST, headers);
    }

    public static String requestGet(String url, String param, Map<String, String> headers) throws Exception {
        return connect(url, param, HttpMethod.GET, headers);
    }

    /**
     * 通过 get 或者 post 方式获取请求
     *
     * @param url    post 请求，此时参数从 body 中传过来,body 为 json 类型
     * @param params 如果没有则为空
     */
    private static String connect(String url, String params, HttpMethod httpMethod, Map<String, String> headers) throws Exception {
        boolean isSSL = false;
        if (url.startsWith("https://")) {
            isSSL = true;
            _ignoreSSL();
        }

        HttpURLConnection conn = null;
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuffer result = new StringBuffer();

        try {
            if ("POST".equals(httpMethod.name())) {
                URL urlEntity = new URL(url);
                conn = (HttpURLConnection) urlEntity.openConnection();
            } else if ("GET".equalsIgnoreCase(httpMethod.name())) {
                if (params == null) {
                    URL urlEntity = new URL(url);
                    conn = (HttpURLConnection) urlEntity.openConnection();
                } else {
                    URL urlEntity = new URL(url + "?" + params);
                    conn = (HttpURLConnection) urlEntity.openConnection();
                }
            } else {
                logger.error("unsuppport http method. {} {}", httpMethod.name(), url);
            }

            if (headers != null && headers.containsKey("Content-Type")) {
                conn.setRequestProperty("Content-Type", headers.get("Content-Type"));
                headers.remove("Content-Type");
            } else {
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            }

            conn.setRequestMethod(httpMethod.name());
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            if (StringUtils.hasText(params)) {
                conn.setRequestProperty("Content-Length", "" + params.length());
            }
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    String headerName = entry.getKey();
                    String headerValue = entry.getValue();
                    conn.setRequestProperty(headerName, headerValue);
                    if (logger.isDebugEnabled()) {
                        logger.debug("header {} {}", headerName, headerValue);
                    }
                }
            }

            if ("POST".equals(httpMethod.name())) {
                if (StringUtils.hasText(params)) {
                    out = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
                    out.write(params);
                    out.close();
                }
            } else {
                conn.connect();
            }

            if (logger.isDebugEnabled()) {
                logger.debug(result.toString());
            }

            String line = "";
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            while ((line = in.readLine()) != null) {
                result.append(line).append("\n");
            }

            if (logger.isDebugEnabled()) {
                logger.debug("response {}", result.toString());
            }

            int responseCode = conn.getResponseCode();
            if (logger.isDebugEnabled()) {
                logger.debug("当前的 response code 为： [ " + responseCode + " ] ");
            }

            // log.info("该请求发送的地址为： [ " + url + " ] ，参数为： [ " + params + " ] ");
            // log.info("responsecode为：" + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // for (Map.Entry<String, List<String>> entries :
                //         conn.getRequestProperties().entrySet()) {
                //     String values = "";
                //     for (String value : entries.getValue()) {
                //         values += value + ",";
                //     }
                //     logger.info("Request {}", entries.getKey() + " - " + values);
                // }
                for (Map.Entry<String, List<String>> entries : conn.getHeaderFields().entrySet()) {
                    String values = "";
                    for (String value : entries.getValue()) {
                        values += value + ",";
                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug("Response {} - {}", entries.getKey(), values);
                    }
                }
            } else if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {

            } else if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST && responseCode < HttpURLConnection.HTTP_INTERNAL_ERROR) {
                throw new IOException("4XX错误，服务器内部错误，该错误需要启动定时任务，并且请通知责任人检查服务器，地址为： [ " + url + " ] ，参数为： [ " + params + " ] ");

            } else if (responseCode >= HttpURLConnection.HTTP_INTERNAL_ERROR && responseCode < 600) {
                throw new IOException("5XX错误，地址为： [ " + url + " ] ，参数为： [ " + params + " ] ， 的程序内部错误，准备启动定时回调任务。");
            }
        } catch (IOException e) {
            StringBuffer errorMsg = new StringBuffer();
            String line = "";
            in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                errorMsg.append(line).append("\n");
            }
            throw new ConnTimeOutException("连接错误，启动定时回调任务，连接地址为：[ " + url + " ] ，参数为： [ " + params + " ] 异常原因[" + errorMsg + "]", e);
        } catch (Exception e) {
            throw new Exception("程序异常，连接地址为：[ " + url + " ] ，参数为： [ " + params + " ]，该异常不需要启动定时任务 ", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result.toString();
    }

    /**
     * 忽略SSL
     */
    private static void _ignoreSSL() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts =
                    new TrustManager[]{
                            new X509TrustManager() {
                                @Override
                                public X509Certificate[] getAcceptedIssuers() {
                                    return null;
                                }

                                @Override
                                public void checkClientTrusted(
                                        X509Certificate[] certs, String authType) {
                                }

                                @Override
                                public void checkServerTrusted(
                                        X509Certificate[] certs, String authType) {
                                }
                            }
                    };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(
                    new HostnameVerifier() {
                        @Override
                        public boolean verify(String s, SSLSession sslsession) {
                            return true;
                        }
                    });
        } catch (KeyManagementException ex) {

        } catch (NoSuchAlgorithmException ex) {

        }
    }
}
