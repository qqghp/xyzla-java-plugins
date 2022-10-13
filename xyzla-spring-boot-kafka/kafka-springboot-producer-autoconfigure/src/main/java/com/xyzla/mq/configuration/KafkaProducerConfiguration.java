package com.xyzla.mq.configuration;

import com.xyzla.mq.Producer;
import com.xyzla.mq.kafka.KafkaProducerImpl;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.Properties;

/**
 * kafka 生产者 配置
 */
@Configuration
//@ConditionalOnProperty(name = "kafka.producer.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaProducerConfiguration {

    private static Logger logger = LoggerFactory.getLogger(KafkaProducerConfiguration.class);

    @Autowired
    KafkaContext kafkaContext;

    @Bean(destroyMethod = "close")
    public KafkaProducer<String, String> kafkaProducer() {
        String clientId = kafkaContext.getProducerClientId();
        logger.info("kafka producer ClientId {} bootstrapServers {}", clientId, kafkaContext.getBootstrapServers());

        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContext.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "-1");
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 25);
        props.put(ProducerConfig.SEND_BUFFER_CONFIG, 131072);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 1048576);
        props.put(ProducerConfig.RETRIES_CONFIG, 2);
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 30000);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "zstd");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        logger.info("kafka KafkaProducer<String,String> 初始化完成 ... {}", producer);
        return producer;
    }

    @Bean
    @DependsOn("kafkaProducer")
    public Producer producer() {
        return new KafkaProducerImpl();
    }
}
