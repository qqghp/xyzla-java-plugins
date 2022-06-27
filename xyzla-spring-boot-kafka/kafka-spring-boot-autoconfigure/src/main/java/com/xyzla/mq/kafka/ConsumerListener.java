package com.xyzla.mq.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消费者监听类,负责 构建 {@link ConsumerHandler ConsumerHandler} 对象, 并启动消费<br/>
 * 可以理解成是单例的
 */
public class ConsumerListener<T> {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerListener.class);

    private ConsumerHandler consumerHandler;

    public ConsumerListener(ConsumerProperty consumerProperty) {
        consumerHandler = new ConsumerHandler(consumerProperty);
    }

    public ConsumerHandler startListen() {
        consumerHandler.execute(5);
        return consumerHandler;
    }

}