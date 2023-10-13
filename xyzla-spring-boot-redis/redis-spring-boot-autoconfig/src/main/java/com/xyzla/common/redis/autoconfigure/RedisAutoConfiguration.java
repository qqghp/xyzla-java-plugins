package com.xyzla.common.redis.autoconfigure;

import com.xyzla.common.redis.template.RedisTemplateConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisOperations;

/**
 * TODO redis exposes export configuration class to the outside world<br>
 * TODO:只有当 classpath 路径下发现 RedisOperations 类才会进行导入@Import下的配置类进入容器
 */
@ConditionalOnClass(RedisOperations.class)
@Import(
        value = {
                RedisClusterLettuceConfiguration.class,
                RedisLettuceConfiguration.class,
                RedisProperties.class,
                RedisClusterProperties.class,
                RedisTemplateConfiguration.class
        })
public class RedisAutoConfiguration {
}
