package com.xyzla.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SpringConfiguration.class);

    @Bean("springUtils")
    public SpringUtils springUtils() {
        logger.info("SpringUtils initialization ...");
        return new SpringUtils();
    }
}
