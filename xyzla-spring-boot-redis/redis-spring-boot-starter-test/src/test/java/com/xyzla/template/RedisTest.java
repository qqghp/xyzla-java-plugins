package com.xyzla.template;

import com.xyzla.redis.RedisApplication;
import com.xyzla.redis.entity.Device;
import com.xyzla.redis.util.JacksonUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = RedisApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {RedisTemplateConfiguration.class, RedisProperties.class, RedisLettuceConfiguration.class})
@EnableAutoConfiguration
@ActiveProfiles("dev")
public class RedisTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private static final String SHOWMAC_USERLIST_TOP = "SHOWMAC:USERLIST:TOP";
    private static final String SHOWMAC_USERLIST_RANK = "SHOWMAC:USERLIST:RANK";

    private static final String SHOWMAC_USER_HASH_TOP = "SHOWMAC:USERHASH:TOP";
    private static final String SHOWMAC_USER_HASH_RANK = "SHOWMAC:USERHASH:RANK";

    @Test
    public void redisTemplateList() {
        redisTemplate.opsForList().leftPush(SHOWMAC_USERLIST_TOP, "wanggang");
        redisTemplate.opsForList().leftPush(SHOWMAC_USERLIST_TOP, "wangsan");
        redisTemplate.opsForList().leftPush(SHOWMAC_USERLIST_TOP, "wangermazi");
        System.out.println((String) redisTemplate.opsForList().rightPop(SHOWMAC_USERLIST_TOP));
        System.out.println((String) redisTemplate.opsForList().rightPop(SHOWMAC_USERLIST_TOP));
        //
        // System.out.println((String)redisTemplate.opsForList().rightPop(SHOWMAC_USERLIST_TOP));

    }

    @Test
    public void stringRedisTemplateList() {
        stringRedisTemplate.opsForList().leftPush(SHOWMAC_USERLIST_RANK, "lisi");
        stringRedisTemplate.opsForList().leftPush(SHOWMAC_USERLIST_RANK, "liwu");
        stringRedisTemplate.opsForList().leftPush(SHOWMAC_USERLIST_RANK, "liliu");
        System.out.println(
                (String) stringRedisTemplate.opsForList().rightPop(SHOWMAC_USERLIST_RANK));
        System.out.println(
                (String) stringRedisTemplate.opsForList().rightPop(SHOWMAC_USERLIST_RANK));
        //
        // System.out.println((String)stringRedisTemplate.opsForList().rightPop(SHOWMAC_USER_RANK));

    }

    @Test
    public void redisTemplateHash() {
        redisTemplate
                .opsForHash()
                .put(SHOWMAC_USER_HASH_TOP, "1", new Device(1, "111", "111", "111"));
        redisTemplate
                .opsForHash()
                .put(SHOWMAC_USER_HASH_TOP, "2", new Device(2, "222", "222", "222"));
        redisTemplate
                .opsForHash()
                .put(SHOWMAC_USER_HASH_TOP, "3", new Device(3, "333", "333", "333"));
        System.out.println(JacksonUtil.toJson(redisTemplate.opsForHash().get(SHOWMAC_USER_HASH_TOP, "1")));
        System.out.println(JacksonUtil.toJson(redisTemplate.opsForHash().get(SHOWMAC_USER_HASH_TOP, "1")));
    }

    @Test
    public void stringRedisTemplateHash() {
        stringRedisTemplate
                .opsForHash()
                .put(
                        SHOWMAC_USER_HASH_RANK,
                        "1",
                        JacksonUtil.toJson(new Device(1, "111", "111", "111")));
        stringRedisTemplate
                .opsForHash()
                .put(
                        SHOWMAC_USER_HASH_RANK,
                        "2",
                        JacksonUtil.toJson(new Device(2, "222", "222", "222")));
        stringRedisTemplate
                .opsForHash()
                .put(
                        SHOWMAC_USER_HASH_RANK,
                        "3",
                        JacksonUtil.toJson(new Device(3, "333", "333", "333")));
        System.out.println(
                (String) stringRedisTemplate.opsForHash().get(SHOWMAC_USER_HASH_RANK, "1"));
        System.out.println(
                (String) stringRedisTemplate.opsForHash().get(SHOWMAC_USER_HASH_RANK, "2"));
    }

    @Test
    public void redisTemplateHashPutAll() {
        List<Device> list = new ArrayList<>();
        list.add(new Device(1, "111", "111", "111"));
        list.add(new Device(2, "222", "222", "222"));
        list.add(new Device(3, "333", "333", "333"));

        Map<String, Device> iccidMap = new HashMap<>(1024);

        for (Device d : list) {
            iccidMap.put(d.getIccid().toUpperCase(), d);
        }

        redisTemplate.opsForHash().putAll(SHOWMAC_USER_HASH_TOP, iccidMap);

        System.out.println(JacksonUtil.toJson(redisTemplate.opsForHash().get(SHOWMAC_USER_HASH_TOP, "111")));
    }
}
