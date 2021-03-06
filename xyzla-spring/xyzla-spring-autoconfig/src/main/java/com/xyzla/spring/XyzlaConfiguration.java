package com.xyzla.spring;

import com.opencsv.CSVWriter;
import com.xyzla.spring.service.CsvService;
import com.xyzla.spring.service.MongoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XyzlaConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(XyzlaConfiguration.class);

    @Bean("csvService")
    @ConditionalOnClass(CSVWriter.class)
    public CsvService csvService() {
        logger.info("CsvService initialization ...");
        return new CsvService();
    }

    @Bean("mongoService")
    @ConditionalOnBean(name = "mongoTemplate")
    public MongoService mongoService() {
        logger.info("MongoService initialization ...");
        return new MongoService();
    }
}
