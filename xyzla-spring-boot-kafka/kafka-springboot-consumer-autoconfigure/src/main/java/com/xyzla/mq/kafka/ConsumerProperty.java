package com.xyzla.mq.kafka;

import java.util.HashMap;
import java.util.Map;

public class ConsumerProperty {

    private String brokerList;

    private String groupId;

    private Map<String, TopicConfig> topicConfigMap = new HashMap<>(128);

    public ConsumerProperty() {
    }

    public ConsumerProperty(String brokerList, String groupId) {
        this.brokerList = brokerList;
        this.groupId = groupId;
    }

    public ConsumerProperty(String brokerList, String groupId, Map<String, TopicConfig> topicConfigMap) {
        this.brokerList = brokerList;
        this.groupId = groupId;
        this.topicConfigMap = topicConfigMap;
    }

    public String getBrokerList() {
        return brokerList;
    }

    public String getGroupId() {
        return groupId;
    }

    public Map<String, TopicConfig> getTopicConfigMap() {
        return topicConfigMap;
    }
}