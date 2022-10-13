package com.xyzla.mq.kafka;

import com.xyzla.mq.Consumer;

public class MessageContainer {

    private String topic;

    private Consumer messageHandle;


    public MessageContainer() {
    }

    public MessageContainer(String topic, Consumer messageHandle) {
        this.topic = topic;
        this.messageHandle = messageHandle;
    }

    public String getTopic() {
        return topic;
    }

    public Consumer getMessageHandle() {
        return messageHandle;
    }

}