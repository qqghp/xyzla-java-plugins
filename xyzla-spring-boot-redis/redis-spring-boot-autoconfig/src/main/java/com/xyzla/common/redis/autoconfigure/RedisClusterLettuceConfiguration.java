package com.xyzla.common.redis.autoconfigure;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
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

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Configuration
//@ConditionalOnClass(AbstractRedisClient.class)
@ConditionalOnProperty(prefix = "spring.redis.cluster", name = "mode", havingValue = "cluster")
public class RedisClusterLettuceConfiguration {


    /**
     * https://support.huaweicloud.com/intl/en-us/usermanual-dcs/dcs-ug-211203001.html
     *
     * @param redisProperties
     * @return
     */
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(RedisProperties redisProperties, RedisClusterProperties redisClusterProperties) {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(redisProperties.getMaxIdle());
        genericObjectPoolConfig.setMinIdle(redisProperties.getMinIdle());
        //genericObjectPoolConfig.setMaxTotal(redisProperties.getMaxTotal());
        genericObjectPoolConfig.setMaxWait(Duration.ofMillis(redisProperties.getMaxWait()));
        genericObjectPoolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(redisProperties.getTimeBetweenEvictionRuns()));
        List<String> nodes = redisClusterProperties.getNodes();
        List<RedisNode> listNodes = new ArrayList();
        for (String node : nodes) {
            String[] ipAndPort = node.split(":");
            RedisNode redisNode = new RedisNode(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
            listNodes.add(redisNode);
        }
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        redisClusterConfiguration.setClusterNodes(listNodes);
        redisClusterConfiguration.setPassword(redisProperties.getPassword());
        redisClusterConfiguration.setMaxRedirects(redisProperties.getMaxRedirects());
        // Configure automated topology refresh.
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(Duration.ofSeconds(redisProperties.getPeriod())) // Refresh the topology periodically.
                .enableAllAdaptiveRefreshTriggers() // Refresh the topology based on events.
                .build();

        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
                // Redis command execution timeout. Only when the command execution times out will a reconnection be triggered using the new topology.
                .timeoutOptions(TimeoutOptions.enabled(Duration.ofSeconds(redisProperties.getPeriod())))
                .topologyRefreshOptions(topologyRefreshOptions)
                .build();
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(redisProperties.getTimeout()))
                .poolConfig(genericObjectPoolConfig)
                .readFrom(ReadFrom.REPLICA_PREFERRED) // Preferentially read data from the replicas.
                .clientOptions(clusterClientOptions)
                .build();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisClusterConfiguration, clientConfig);
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
