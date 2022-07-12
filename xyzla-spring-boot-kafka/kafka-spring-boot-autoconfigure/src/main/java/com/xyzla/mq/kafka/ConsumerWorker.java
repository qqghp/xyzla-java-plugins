package com.xyzla.mq.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.xyzla.common.util.JacksonUtil;
import com.xyzla.mq.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConsumerWorker<T> implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerHandler.class);

    private ConsumerRecord<String, String> consumerRecord;
    private Consumer consumer;

    public ConsumerWorker() {
    }

    public ConsumerWorker(ConsumerRecord record, Consumer consumer) {
        this.consumerRecord = record;
        this.consumer = consumer;
    }

    public void run() {
        try {
            ConsumerMessageBO consumerMessageBO = new ConsumerMessageBO();
            consumerMessageBO.setOffset(consumerRecord.offset());
            consumerMessageBO.setPartition(consumerRecord.partition());
//        for (MessageContainer messageContainer : PropertyFactory.consumerProperty.getMessageContainers()) {
//            if (consumerRecord.topic().equals(messageContainer.getTopic())) {
//                Class<T> tClass = messageContainer.getMessageHandle().getTClass();
//                T t = JacksonUtil.readValue(consumerRecord.value(), tClass);
//                consumerMessageBO.setT(t);
//                messageContainer.getMessageHandle().onMessage(consumerMessageBO);
//                break;
//            }
//        }

            if (consumer == null) {
                logger.error("topic {} message listener not exists.", consumerRecord.topic());
                // TODO
                //throw new Exception("");
            }

            TypeReference typeReference = consumer.getTypeReference();
            T t;
            if (typeReference == null) {
                Class<T> tClass = consumer.getTClass();
                t = JacksonUtil.readValue(consumerRecord.value(), tClass);
            } else {
                t = (T) JacksonUtil.readValue(consumerRecord.value(), typeReference);
            }

            consumerMessageBO.setT(t);
            consumer.onMessage(consumerMessageBO);
        } catch (Exception ex) {
            logger.error("consumer exception. offset {} partition {} value {} ... {}", consumerRecord.offset(), consumerRecord.partition(), consumerRecord.value(), ex);
        }
    }


}