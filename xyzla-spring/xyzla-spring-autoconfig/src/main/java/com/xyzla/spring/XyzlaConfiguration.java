package com.xyzla.spring;

import com.xyzla.spring.service.MongoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XyzlaConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(XyzlaConfiguration.class);
    @Bean("mongoService")
    @ConditionalOnBean(name = "mongoTemplate")
    public MongoService mongoService() {
        logger.info("MongoService initialization ...");
        return new MongoService();
    }
}
