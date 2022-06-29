package com.xyzla.common.monitor;

import com.xyzla.dingbot.monitor.cache.Cache;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class CacheTest {

    @Test
    public void test() throws InterruptedException {
        Cache<Integer, String> cache = new Cache<Integer, String>();
        cache.put(1, "aaaa", 3, TimeUnit.SECONDS);

        // Thread.sleep(1000 * 2);//单位 毫秒
        TimeUnit.SECONDS.sleep(2);

        {
            String str = cache.get(1);
            System.out.println(str);
        }

        //Thread.sleep(1000 * 2);
        TimeUnit.SECONDS.sleep(2);

        {
            String str = cache.get(1);
            System.out.println(str);
        }
    }
}
