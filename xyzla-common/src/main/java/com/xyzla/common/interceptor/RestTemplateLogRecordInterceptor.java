package com.xyzla.common.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * resttemplate 日志记录
 * https://blog.csdn.net/hearain528/article/details/104924978
 */
public class RestTemplateLogRecordInterceptor implements ClientHttpRequestInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(RestTemplateLogRecordInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        LOGGER.debug("===========================request begin================================================");
        LOGGER.debug("URI         : {}", request.getURI());
        LOGGER.debug("Method      : {}", request.getMethod());
        LOGGER.debug("Headers     : {}", request.getHeaders());
        LOGGER.debug("Request body: {}", new String(body, "UTF-8"));
        LOGGER.debug("==========================request end================================================");
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        LOGGER.debug("============================response begin==========================================");

        LOGGER.debug("Raw status code  : {}", response.getRawStatusCode());

        // LOGGER.debug("Status code  : {}", response.getStatusCode());
        // LOGGER.debug("Status text  : {}", response.getStatusText());

        LOGGER.debug("Headers      : {}", response.getHeaders());
        LOGGER.debug("Content-Type : {}", response.getHeaders().getContentType());

        StringBuilder inputStringBuilder = new StringBuilder();
        InputStream body = response.getBody();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(body, "UTF-8"))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                inputStringBuilder.append(line);
                inputStringBuilder.append('\n');
                line = bufferedReader.readLine();
            }
        }

        LOGGER.debug("Response body: {}", inputStringBuilder.toString());
        LOGGER.debug("=======================response end=================================================");
    }


}

