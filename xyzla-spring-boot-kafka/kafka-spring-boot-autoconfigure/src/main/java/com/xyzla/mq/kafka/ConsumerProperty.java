package com.xyzla.mq.kafka;

import java.util.HashMap;
import java.util.Map;

public class ConsumerProperty {

    private String brokerList;

    private String groupId;

    private Integer maxPartitionFetchBytes;
    private Integer maxPollIntervalMs;
    private Integer maxPollRecords;

    private Map<String, TopicConfig> topicConfigMap = new HashMap<>(128);

    public ConsumerProperty() {
    }

    public ConsumerProperty(String brokerList, String groupId) {
        this.brokerList = brokerList;
        this.groupId = groupId;
    }

    public ConsumerProperty(String brokerList,
                            String groupId,
                            Integer maxPartitionFetchBytes,
                            Integer maxPollIntervalMs,
                            Integer maxPollRecords,
                            Map<String, TopicConfig> topicConfigMap) {
        this.brokerList = brokerList;
        this.groupId = groupId;
        this.maxPartitionFetchBytes = maxPartitionFetchBytes;
        this.maxPollIntervalMs = maxPollIntervalMs;
        this.maxPollRecords = maxPollRecords;
        this.topicConfigMap = topicConfigMap;
    }

    public String getBrokerList() {
        return brokerList;
    }

    public String getGroupId() {
        return groupId;
    }

    public Integer getMaxPartitionFetchBytes() {
        return maxPartitionFetchBytes;
    }

    public Integer getMaxPollIntervalMs() {
        return maxPollIntervalMs;
    }

    public Integer getMaxPollRecords() {
        return maxPollRecords;
    }

    public Map<String, TopicConfig> getTopicConfigMap() {
        return topicConfigMap;
    }
}