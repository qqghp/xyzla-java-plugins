package com.xyzla.mq.kafka;

import com.xyzla.common.util.JacksonUtil;
import com.xyzla.mq.Producer;
import com.xyzla.mq.configuration.KafkaContext;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.RetriableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class KafkaProducerImpl implements Producer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerImpl.class);

    @Autowired
    KafkaProducer<String, String> kafkaProducer;

    @Autowired
    KafkaContext kafkaContext;

    public <T> void send(String topic, T t) {
        String messageJson = JacksonUtil.toJson(t);
        this.send(topic, messageJson);
    }

    /**
     * 异步 提交
     *
     * @param topic   主题
     * @param message 消息
     */
    public void send(String topic, String message) {
        String realTopic = kafkaContext.getRealTopic(topic);
        if (logger.isDebugEnabled()) {
            logger.debug("kafka send {} -> {} -> {}", topic, realTopic, message);
        }
        kafkaProducer.send(new ProducerRecord<>(realTopic, message), new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    // 发送成功
                    logger.info("kafka topic {} partition {} offset {} send successfully", realTopic, metadata.partition(), metadata.offset());
                } else {
                    // 发送失败
                    // TODO
                    logger.error("kafka generate a message to server topic {} is abnormal. {} {}", realTopic, message, exception);
                    if (exception instanceof RetriableException) {
                        //处理可重试异常
                    } else {
                        //处理不可重试异常
                    }
                }
            }
        });
    }
}

