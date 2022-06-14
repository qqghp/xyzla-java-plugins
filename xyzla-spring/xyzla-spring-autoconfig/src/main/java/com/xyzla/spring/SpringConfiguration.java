package com.xyzla.spring;

import com.opencsv.CSVWriter;
import com.xyzla.spring.service.CsvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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

    @Bean("csvService")
    @ConditionalOnClass(CSVWriter.class)
    public CsvService csvService() {
        logger.info("CsvService initialization ...");
        return new CsvService();
    }
}
