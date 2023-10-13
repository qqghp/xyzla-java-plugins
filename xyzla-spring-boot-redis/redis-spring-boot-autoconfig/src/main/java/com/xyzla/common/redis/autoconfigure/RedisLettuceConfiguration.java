package com.xyzla.common.redis.autoconfigure;

import jakarta.annotation.Resource;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.time.Duration;

/*
 * {@like @ConditionalOnClass: This annotation indicates that there must be RedisOperations in the
 * current classpath to inject this Bean} @ConditionalOnClass(Jedis.class)
 * 此注解表示当前 ClassPath 必须包含有 Jedis 这个类才会入这个配置类到 spring 容器中 意思就是项目当中存在了 jedis 客户端依赖才觉得你需要使用，否则就没必要去注入.
 */
@Configuration
//@ConditionalOnClass(AbstractRedisClient.class)
@ConditionalOnProperty(prefix = "spring.data.redis", name = "mode", havingValue = "standalone")
public class RedisLettuceConfiguration {

    @Resource
    private RedisProperties redisProperties;

    @Bean
    public LettuceConnectionFactory connectionFactory() {
        System.out.println("connectionFactory init...");
        //定义redis链接池
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(redisProperties.getMaxActive());
        poolConfig.setMaxWaitMillis(redisProperties.getMaxWait());
        poolConfig.setMaxIdle(redisProperties.getMaxIdle());
        poolConfig.setMinIdle(redisProperties.getMinIdle());
        //配置对象
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        if (!StringUtils.isEmpty(redisProperties.getPassword())) {
            redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        }

        // 创建工厂
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(poolConfig).build();

        LettuceConnectionFactory factory =
                new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfiguration);
        factory.afterPropertiesSet();
        return factory;
    }

    /**
     * 选择 redis 作为默认缓存工具
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        //默认1
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                .disableCachingNullValues();
        RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
        return redisCacheManager;
    }

    /**
     * key 类型
     *
     * @return
     */
    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    /**
     * 值采用JSON序列化
     *
     * @return
     */
    private RedisSerializer<Object> valueSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }


}
