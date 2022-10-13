package com.xyzla.mq;

public interface Producer {


    /**
     * MQ 生产者 生产消息
     *
     * @param topic
     * @param <T>
     */
    <T> void send(String topic, T t);
}
