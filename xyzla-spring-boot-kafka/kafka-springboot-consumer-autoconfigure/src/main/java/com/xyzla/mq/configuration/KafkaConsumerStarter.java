package com.xyzla.mq.configuration;

import com.xyzla.mq.Consumer;
import com.xyzla.mq.SpringContextHolder;
import com.xyzla.mq.kafka.*;
import com.xyzla.mq.Listener;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * kafka 消费者 启动类
 */
@Configuration
@DependsOn("springContextHolder")
//@ConditionalOnProperty(value = "kafka.consumer.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaConsumerStarter {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerStarter.class);

    @Autowired
    AdminService adminService;

    @Autowired
    KafkaContext kafkaContext;

    ConsumerHandler consumer = null;

    /**
     * kafka 消费者 启动方法
     */
    @PostConstruct
    public void startListerConsumer() {
        // 获取 注解 (@Listener) 的实现类
        Map<String, Object> beans = SpringContextHolder.getApplicationContext().getBeansWithAnnotation(Listener.class);
        logger.info("consumer size: {}", beans.size());

        Map<String, TopicConfig> topicConfigMap = new HashMap<>(128);

        // 以 <topic,MessageListenerImpl> 的格式写入 map 中
        // 并判断 topic 是否存在, 如果不存在则创建
        beans.values().forEach(e -> {
            if (e instanceof Consumer) {
                Consumer<?> listener = (Consumer<?>) e;

                String realTopic = listener.getRealTopic();
                Integer partitions = listener.partitions();
                Short replicationFactor = listener.replicationFactor();
                ExecutorService executorService = listener.executorService();

                TopicConfig topicConfig = new TopicConfig(realTopic, partitions, replicationFactor, executorService, listener);
                topicConfigMap.put(realTopic, topicConfig);

                logger.info("{}  has been subscribed", listener.getRealTopic());
                adminService.checkAndCreateTopic(realTopic, partitions, replicationFactor);
            }
        });
        ConsumerProperty consumerProperty = new ConsumerProperty(
                kafkaContext.getBootstrapServers(),
                kafkaContext.getGroupId(),
                kafkaContext.getMaxPartitionFetchBytes(),
                kafkaContext.getMaxPollIntervalMs(),
                kafkaContext.getMaxPollRecords(),
                topicConfigMap);

        //启动 kafka 消费者
        consumer = new ConsumerListener(consumerProperty).startListen();
    }

    /**
     * kafka 消费者 销毁时 执行的方法
     */
    @PreDestroy
    public void shutDown() {
        if (consumer != null) {
            consumer.shutdown();
        }
    }
}
