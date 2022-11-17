package com.xyzla.snowflake;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SnowflakeAutoConfiguration.class);

    @Bean("snowflake")
    public Snowflake snowflake(@Value("${snowflake.datacenter-id}") Integer datacenterId,
                               @Value("${snowflake.worker-id}") Integer workerId) {
        logger.info("Snow Flake init ...");
        return new Snowflake(workerId, datacenterId);
    }
}
