package com.xyzla.consumer;

import com.xyzla.mq.Consumer;
import com.xyzla.mq.kafka.ConsumerMessageBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageListener extends Consumer<String> {

    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    @Override
    public void onMessage(ConsumerMessageBO message) {
        String entity = (String) message.getT();
        logger.info("{}:{}:{}   {}", getTopic(), message.getPartition(), message.getOffset(), entity.toString());
    }

    @Override
    public String getTopic() {
        return "SMS";
    }
}
