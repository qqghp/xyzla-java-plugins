package com.showmac.consumer;

import com.showmac.entity.TopicDemoEntity;
import com.xyzla.mq.Consumer;
import com.xyzla.mq.kafka.ConsumerMessageBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TopicDemoListener extends Consumer<TopicDemoEntity> {

    private static final Logger logger = LoggerFactory.getLogger(TopicDemoListener.class);

    @Override
    public void onMessage(ConsumerMessageBO message) {
        TopicDemoEntity entity = (TopicDemoEntity) message.getT();
        logger.info("{}:{}:{}   {}", getTopic(), message.getPartition(), message.getOffset(), entity.toString());
    }

    @Override
    public String getTopic() {
        return "TOPIC_DEMO";
    }
}
