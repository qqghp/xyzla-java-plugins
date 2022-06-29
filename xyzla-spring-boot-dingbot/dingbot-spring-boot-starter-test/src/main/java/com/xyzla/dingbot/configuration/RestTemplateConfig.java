package com.xyzla.dingbot.configuration;

import com.xyzla.dingbot.configuration.interceptor.RestTemplateLogRecordInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
public class RestTemplateConfig {

    private Logger log = LoggerFactory.getLogger(RestTemplateConfig.class);

    @Bean(name = "restTemplate")
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(50000);
        httpRequestFactory.setReadTimeout(90000);
        BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(httpRequestFactory);
        RestTemplate template = new RestTemplate(bufferingClientHttpRequestFactory);
        //日志打印
        template.getInterceptors().add(new RestTemplateLogRecordInterceptor());
        template.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return template;
    }
}
