package com.xyzla.mq;


import com.fasterxml.jackson.core.type.TypeReference;
import com.xyzla.mq.kafka.ConsumerMessageBO;
import com.xyzla.mq.configuration.KafkaContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Listener
public abstract class Consumer<T> {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    protected String topic;

    @Autowired
    private KafkaContext kafkaContext;

    public abstract void onMessage(ConsumerMessageBO message);

    public abstract String getTopic();

    public String getRealTopic() {
        return kafkaContext.getRealTopic(getTopic());
    }

    /**
     * 通过 继承 + 反射 的方法，来的到 T.class
     *
     * @return
     */
    public Class<T> getTClass() {
        Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

    public TypeReference getTypeReference() {
        return null;
    }

    /**
     * 默认分区数为 3
     *
     * @return 返回 分区数
     */
    public Integer partitions() {
        return 3;
    }

    /**
     * 默认 副本数 为 3
     *
     * @return 返回 副本数
     */
    public Short replicationFactor() {
        return 3;
    }

    /**
     * 为 每个 Topic 单独单独配置线程池, 默认线程池 参数如下:
     * - 核心线程数 3
     * - 最大线程数 30
     * - keepAliveTime 30 Minutes
     * - 工作队列: ArrayBlockingQueue 为 1
     * - 自定义线程池 工厂, 重新命名 线程名
     * - 拒绝策略: ThreadPoolExecutor.CallerRunsPolicy
     *
     * @return 返回 自定义 线程池
     */
    public ExecutorService executorService() {
        ExecutorService es = new ThreadPoolExecutor(
                5,
                30,
                30L,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue(1),
                new NameTreadFactory(getTopic()),
                new ThreadPoolExecutor.CallerRunsPolicy());

        return es;
    }

    /**
     * 线程池 工厂, 主要作用是給线程 (自定义) 命名
     */
    private static class NameTreadFactory implements ThreadFactory {

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

}