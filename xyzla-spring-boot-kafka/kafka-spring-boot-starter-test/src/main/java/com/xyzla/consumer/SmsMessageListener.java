package com.xyzla.consumer;

import com.xyzla.entity.SmsMessageEntity;
import com.xyzla.mq.Consumer;
import com.xyzla.mq.kafka.ConsumerMessageBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SmsMessageListener extends Consumer<SmsMessageEntity> {

    private static final Logger logger = LoggerFactory.getLogger(SmsMessageListener.class);

    @Override
    public void onMessage(ConsumerMessageBO message) {
        SmsMessageEntity entity = (SmsMessageEntity) message.getT();
        logger.info("{}:{}:{}   {}", getTopic(), message.getPartition(), message.getOffset(), entity.toString());
    }

    @Override
    public String getTopic() {
        return "SMS_RECORD";
    }


}
