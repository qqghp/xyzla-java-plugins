package com.xyzla.mq.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;

//@Component
//@ConditionalOnBean(AdminClient.class)
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    AdminClient adminClient;


    public AdminService() {
    }

    public AdminService(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    public void checkAndCreateTopic(String topic, Integer partitions, Short replicationFactor) {
        try {
            ListTopicsResult listTopics = adminClient.listTopics();
            Set<String> names = listTopics.names().get();
            boolean contains = names.contains(topic);
            if (!contains) {
                logger.info("topic {} not exists.", topic);
                Map<String, String> configs = new HashMap<String, String>();
                NewTopic newTopic = new NewTopic(topic, partitions, replicationFactor).configs(configs);
                CreateTopicsResult result = adminClient.createTopics(Collections.singleton(newTopic));
                try {
                    result.all().get();
                    logger.info("topic {} partitions {} replicationFactor {} created successfully.", topic, partitions, replicationFactor);
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("topic {} partitions {} replicationFactor {} created failed.", topic, partitions, replicationFactor, e);
                }
            }
        } catch (Exception ex) {
            logger.error("topic {} partitions {} replicationFactor {} create failed.", topic, partitions, replicationFactor, ex);
        }
    }
}
