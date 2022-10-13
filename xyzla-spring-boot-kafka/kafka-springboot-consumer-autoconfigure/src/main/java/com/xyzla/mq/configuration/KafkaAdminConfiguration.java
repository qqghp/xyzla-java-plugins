package com.xyzla.mq.configuration;

import com.xyzla.mq.kafka.AdminService;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


@Configuration
public class KafkaAdminConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(KafkaAdminConfiguration.class);

    @Autowired
    KafkaContext kafkaContext;

    @Bean("adminClient")
    public AdminClient adminClient() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContext.getBootstrapServers());
        props.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        AdminClient adminClient = AdminClient.create(props);
        logger.info("kafka AdminClient 初始化完成 ... {}", adminClient);
        return adminClient;
    }

    @Bean("adminService")
    public AdminService adminService(@Qualifier("adminClient") AdminClient adminClient) {
        return new AdminService(adminClient);
    }
}

