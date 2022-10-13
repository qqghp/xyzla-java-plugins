package com.xyzla.mq.configuration;

import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * kafka context
 */
@Configuration
public class KafkaContext {

    private static Logger logger = LoggerFactory.getLogger(KafkaContext.class);

    /**
     * 生产者客户端 连接 Kafka 集群所需的 broker 地址列表
     * <p>
     * 格式: host1:port1,host2:port2
     */
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * 消费群组 ID
     */
    @Value("${kafka.consumer.group-id}")
    private String groupId;

    /**
     * MQ Topic 前缀, 主要用于区分不同环境, 该值通常取自 {spring.profiles.active}
     */
    @Value("${mq.topic.prefix:}")
    private String prefix;

    /**
     * 客户端前缀
     */
    @Value("${kafka.client-mark}")
    private String clientMark;


    /**
     * 获取 Topic 的前缀
     *
     * @return 返回 Topic 前缀 (如果不为空, 则全部转为 大写)
     */
    public String getPrefix() {
        // 为空 或 全为空白字符 则返回 空, 否则 返回 全大写的 PREFIX
        if (StringUtils.isBlank(prefix)) {
            return "";
        } else {
            //return prefix.trim().toUpperCase();
            return prefix.trim();
        }
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getClientMark() {
        return clientMark;
    }

    /**
     * 获取 real topic
     * <p>
     * 1. 定义在常量里面的 topic
     * 2. kafka 真实的 Topic=${spring.profiles.active}+常量Topic
     *
     * @param originalTopic 定义为常量
     * @return 返回 kafka 实际 Topic
     */
    public String getRealTopic(String originalTopic) {
        if (StringUtils.isNotBlank(getPrefix())) {
            if (getPrefix().contains("production")) {
                return "production" + "_" + originalTopic;
            } else if (getPrefix().contains("test")) {
                return "test" + "_" + originalTopic;
            } else if (getPrefix().contains("dev")) {
                return "dev" + "_" + originalTopic;
            }
        }
        return Strings.isNullOrEmpty(getPrefix()) ? originalTopic : getPrefix() + "_" + originalTopic;
    }


    /**
     * 获取生产者 Client Id
     *
     * @return 返回 生产者 Client Id
     */
    public String getProducerClientId() {
        String clientId = "";
        // TODO
        int resRandom = new Random().nextInt((9999 - 100) + 1) + 10;
        clientId = new StringBuilder("PRODUCER_").append(clientMark).append("_").append(resRandom).toString();

        logger.info("kafka producer clientId {}", clientId);
        return clientId;
    }

    /**
     * 获取 消费者 Client Id
     *
     * @return 返回 消费者 Client Id
     */
    public String getConsumerClientId() {
        String clientId = "";
        // TODO 分布式 ID
        int resRandom = new Random().nextInt((9999 - 100) + 1) + 10;
        clientId = new StringBuilder("CONSUMER_").append(clientMark).append("_").append(resRandom).toString();
        logger.info("kafka consumer clientId {}", clientId);
        return clientId;
    }
}
