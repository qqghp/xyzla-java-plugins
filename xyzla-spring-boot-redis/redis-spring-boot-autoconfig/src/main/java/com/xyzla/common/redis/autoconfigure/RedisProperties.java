package com.xyzla.common.redis.autoconfigure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * configuration must begin with this prefix 限定使用此 starter 的 redis 配置必须以 “spring.redis.” 为前缀 示例:
 * <br>
 * spring.redis.host<br>
 * spring.redis.port
 */
@Component
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {

    /**
     * standalone
     * cluster
     * sentinel
     */
    @Value("${spring.data.redis.mode:standalone")
    private String mode;

    @Value("${spring.data.redis.port:6379}")
    private int port;
    @Value("${spring.data.redis.host:127.0.0.1}")
    private String host;

    @Value("${spring.data.redis.password:}")
    private String password;

    @Value("${spring.data.redis.timeout:600}")
    private int timeout;

    @Value("${spring.data.redis.database:0}")
    private int database;

    @Value("${spring.data.redis.lettuce.pool.max-active:50}")
    private int maxActive;

    @Value("${spring.data.redis.lettuce.pool.max-wait:5000}")
    private int maxWait;

    @Value("${spring.data.redis.lettuce.pool.max-idle:50}")
    private int maxIdle;

    @Value("${spring.data.redis.lettuce.pool.min-idle:10}")
    private int minIdle;

    @Value("${spring.data.redis.lettuce.pool.time-between-eviction-runs:2000}")
    private int timeBetweenEvictionRuns;

    @Value("${spring.data.redis.lettuce.cluster.refresh.period:60}")
    private int period;

    @Value("${spring.data.redis.lettuce.cluster.refresh.period:3}")
    private int maxRedirects;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getTimeBetweenEvictionRuns() {
        return timeBetweenEvictionRuns;
    }

    public void setTimeBetweenEvictionRuns(int timeBetweenEvictionRuns) {
        this.timeBetweenEvictionRuns = timeBetweenEvictionRuns;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getMaxRedirects() {
        return maxRedirects;
    }

    public void setMaxRedirects(int maxRedirects) {
        this.maxRedirects = maxRedirects;
    }
}
