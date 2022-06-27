package com.xyzla.mq.configuration;

import com.xyzla.mq.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SpringConfiguration.class);

    @Bean("springContextHolder")
    public SpringContextHolder springContextHolder() {
        logger.info("springContextHolder initialization ...");
        return new SpringContextHolder();
    }

}
