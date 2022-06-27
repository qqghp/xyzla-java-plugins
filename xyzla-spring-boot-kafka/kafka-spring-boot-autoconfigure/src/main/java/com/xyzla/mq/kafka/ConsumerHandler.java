package com.xyzla.mq.kafka;

import com.xyzla.mq.SpringContextHolder;
import com.xyzla.mq.configuration.KafkaContext;
import com.xyzla.mq.exception.ConsumerUnconfiguredException;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * https://www.programminghunter.com/article/2578139501/
 * https://gjtmaster.com/2018/09/17/kafka rebalance 机制与Consumer多种消费模式案例应用实战/
 */
public class ConsumerHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerHandler.class);

    private final KafkaConsumer<String, String> kafkaConsumer;

    private Map<String, ExecutorService> executorServices = new HashMap<String, ExecutorService>(128);
    private Map<String, com.xyzla.mq.Consumer> consumerMap = new HashMap<>(128);

    public ConsumerHandler(ConsumerProperty consumerProperty) {
        String clientId = SpringContextHolder.getBean(KafkaContext.class).getConsumerClientId();
        Set<String> topics = consumerProperty.getTopicConfigMap().keySet();
        initThreadPoolAndConsumer(consumerProperty.getTopicConfigMap());

        Properties props = new Properties();
        // 设置 Kafka 服务器地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerProperty.getBrokerList());
        // 设置 gorup
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProperty.getGroupId());
        // 设置 client id
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);

        // 设置数据 key 的反序列化处理类
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 设置数据 value 的反序列化处理类
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // https://blog.csdn.net/timothytt/article/details/119175571

        // 拉取时间间隔, 默认值: 300 秒; 每次拉取的记录必须在该时间内消费完
        // props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "100000"); // 100 秒
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "300000"); // 5 分钟
        // 每次拉取条数, 默认值: 500 条; 这个条数一定要结合业务背景合理设置
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "10"); // 每一批数据 10 条
        // 每次拉取最大等待时间；时间达到或者消息大小谁先满足条件都触发，没有消息但时间达到返回空消息体
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, "4096"); // 4KB
        //向协调器发送心跳的时间间隔, 默认值: 3 秒; 建议不超过 session.timeout.ms 的1/3
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, "3000"); // 3 秒
        // 心跳超时时间,  默认值: 30 秒; 配置太大会导致真死消费者检测太慢
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000"); // 30 秒

        kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(topics);
    }

    /**
     * 开启一个线程池 ThreadPoolExecutor，建立一个长连接，每 200 毫秒去 kafka 服务器拉取消息，每拉到一个消息，就分配给一个线程类 ConsumerWorker 去处理这个消息
     * 这里要特别注意是，监听 kafka 的过程需要另起一个线程去监听，不然主线程会一直在 while(true)里面阻塞掉
     *
     * @param workerNum
     */
    public void execute(int workerNum) {
        Thread t = new Thread(new Runnable() {//启动一个子线程来监听kafka消息
            public void run() {
                try {
                    while (true) {
                        try {
//                        ConsumerRecords<String, String> records = consumer.poll(200);
//                        for (final ConsumerRecord record : records) {
//                            //System.out.println("监听到kafka消息。。。。。。");
//                            // executors.submit(new ConsumerWorker(record));
//                            executorServices.get(record.topic()).submit(new ConsumerWorker(record));
//
//                            long offset = record.offset();
//                            TopicPartition partition =
//                                    new TopicPartition(record.topic(), record.partition());
//                            consumer.commitSync(Collections
//                                    .singletonMap(partition, new OffsetAndMetadata(offset + 1)));
//                        }

                            // http://www.voycn.com/article/yizhongkafkamokuaidefengzhuang-1
                            // ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
                            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(10));
                            // 对每一个分区的内容
                            for (TopicPartition partition : records.partitions()) {
                                int count = 0;
                                // 获取分区中的 record
                                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                                // 对于每一个 record 数据
                                for (ConsumerRecord<String, String> record : partitionRecords) {
                                    // TimeUnit.MILLISECONDS.sleep(200);
                                    String topic = record.topic();
                                    com.xyzla.mq.Consumer consumer = consumerMap.get(topic);
                                    if (logger.isDebugEnabled()) {
                                        logger.debug(partition + ": " + record.offset() + ": " + record.value() + ": " + (++count) + " / " + partitionRecords.size());
                                    }

                                    executorServices.get(record.topic()).submit(new ConsumerWorker(record, consumer));
                                }
                                // 执行成功, 则取出当前消费到的 offset
                                long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                                // 由于下一次开始消费的位置是最后一次 offset + 1 的位置，所以这里要 + 1
                                // 同步分区的最后的 offset
                                kafkaConsumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                            }
                        } catch (Exception ex) {
                            logger.error("", ex);
                        }
                    }
                } catch (Exception ex) {
                    logger.error("{}", ex);
                } finally {
                    kafkaConsumer.close();
                }
            }
        });
        t.start();
    }

    public void shutdown() {
        if (kafkaConsumer != null) {
            kafkaConsumer.close();
        }
        Set<String> topics = kafkaConsumer.subscription();
        topics.forEach(e -> {
            ExecutorService executors = executorServices.get(e);
            if (executors != null) {
                executors.shutdown();
            }
            try {
                if (!executors.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.out.println("Timeout.... Ignore for this case");
                }
            } catch (InterruptedException ignored) {
                System.out.println("Other thread interrupted this shutdown, ignore for this case.");
                Thread.currentThread().interrupt();
            }
        });
    }

    public void initThreadPoolAndConsumer(Map<String, TopicConfig> topicConfigMap) {
        if (CollectionUtils.isEmpty(topicConfigMap)) {
            logger.error("开启了消费者, 却尚未配置待消费的 Topic");
            throw new ConsumerUnconfiguredException("开启了消费者, 却尚未配置待消费的 Topic");
        }
        topicConfigMap.forEach((topic, config) -> {
            executorServices.put(topic, config.getExecutorService());
            consumerMap.put(topic, config.getConsumer());
        });

    }

    /**
     * 线程池 工厂, 主要作用是給线程 (自定义) 命名
     */
    static class NameTreadFactory implements ThreadFactory {

        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        public NameTreadFactory() {
        }

        private String topic;

        public NameTreadFactory(String topic) {
            this.topic = topic;
        }

        @Override
        public Thread newThread(Runnable r) {
            // 如果达到最大值 则从 1 开始 计数
            if (mThreadNum.get() == Integer.MAX_VALUE) {
                mThreadNum.set(1);
            }
            String threadName = new StringBuilder("CONSUMER_").append(topic).append("_").append(mThreadNum.getAndIncrement()).toString();
            Thread t = new Thread(r, threadName);
            logger.info("{} has been created", t.getName());
            return t;
        }
    }

    public static void displayThreadPoolStatus(ThreadPoolExecutor threadPool, String threadPoolName, long period, TimeUnit unit) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            logger.info("[>>ExecutorStatus<<] ThreadPool Name: [{}], Pool Status: [shutdown={}, Terminated={}], Pool Thread Size: {}, Active Thread Count: {}, Task Count: {}, Tasks Completed: {}, Tasks in Queue: {}",
                    threadPoolName,
                    threadPool.isShutdown(), threadPool.isTerminated(), // 线程是否被终止
                    threadPool.getPoolSize(), // 线程池线程数量
                    threadPool.getActiveCount(), // 工作线程数
                    threadPool.getTaskCount(), // 总任务数
                    threadPool.getCompletedTaskCount(), // 已完成的任务数
                    threadPool.getQueue().size()); // 线程池中线程的数量
        }, 0, period, unit);
    }

}