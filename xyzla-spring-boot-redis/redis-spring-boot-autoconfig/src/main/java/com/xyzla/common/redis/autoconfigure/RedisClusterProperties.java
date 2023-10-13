package com.xyzla.common.redis.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * configuration must begin with this prefix 限定使用此 starter 的 redis 配置必须以 “spring.redis.” 为前缀 示例:
 * <br>
 * spring.redis.host<br>
 * spring.redis.port
 */
@Component
@ConfigurationProperties(prefix = "spring.data.redis.cluster")
public class RedisClusterProperties {

    /**
     * spring.data.redis.cluster.nodes[0] = 127.0.0.1:7379
     * spring.data.redis.cluster.nodes[1] = 127.0.0.1:7380
     */
    private List<String> nodes;

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }
}
