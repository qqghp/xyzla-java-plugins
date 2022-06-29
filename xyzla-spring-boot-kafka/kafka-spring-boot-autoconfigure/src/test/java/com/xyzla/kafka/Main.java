package com.xyzla.kafka;


import com.xyzla.mq.kafka.ConsumerHandler;
import com.xyzla.mq.kafka.ConsumerProperty;

public class Main {
    public static void main(String[] args) {

        String brokerList = "192.168.31.81:9092,192.168.31.82:9092,192.168.31.83:9092";
        String groupId = "xyzla-sms-group";
        String topic = "topic-demo";

        ConsumerProperty consumerProperty = new ConsumerProperty(brokerList, groupId);
        int workerNum = 5;

        ConsumerHandler consumers = new ConsumerHandler(consumerProperty);
        consumers.execute(workerNum);
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException ignored) {
        }
        consumers.shutdown();
    }
}