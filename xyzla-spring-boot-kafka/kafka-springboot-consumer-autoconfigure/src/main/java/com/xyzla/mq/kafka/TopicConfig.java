package com.xyzla.mq.kafka;

import com.xyzla.mq.Consumer;

import java.util.concurrent.ExecutorService;

public class TopicConfig {
    private String topic;
    private Integer partitions;
    private Short replicationFactor;
    private ExecutorService executorService;
    private Consumer consumer;

    private TopicConfig() {
    }

    public TopicConfig(String topic, Integer partitions, Short replicationFactor, ExecutorService executorService, Consumer consumer) {
        this.topic = topic;
        this.partitions = partitions;
        this.replicationFactor = replicationFactor;
        this.executorService = executorService;
        this.consumer = consumer;
    }

    public String getTopic() {
        return topic;
    }

    public Integer getPartitions() {
        return partitions;
    }

    public Short getReplicationFactor() {
        return replicationFactor;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    @Override
    public String toString() {
        return "TopicConfig{" +
                "topic='" + topic + '\'' +
                ", partitions=" + partitions +
                ", replicationFactor=" + replicationFactor +
                ", executorService=" + executorService +
                ", consumer=" + consumer +
                '}';
    }
}
