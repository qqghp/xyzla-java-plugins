package com.xyzla.common;

import com.xyzla.dingbot.DingTalkRobotApplication;
import com.xyzla.dingbot.monitor.FlowMonitor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = DingTalkRobotApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
public class FlowMonitorTest {

    private static Logger logger = LoggerFactory.getLogger(FlowMonitor.class);

    @Autowired
    FlowMonitor flowMonitor;

    @Test
    public void test() {
        LocalDateTime localDateTime = LocalDateTime.now();
        logger.debug("" + flowMonitor.getExpireSecond(localDateTime));
        localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 59, 59).withNano(123));
        logger.debug("" + flowMonitor.getExpireSecond(localDateTime));
        localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0, 0).withNano(123));
        logger.debug("" + flowMonitor.getExpireSecond(localDateTime));
        localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 59, 59).withNano(123));
        logger.debug("" + flowMonitor.getExpireSecond(localDateTime));
        localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0, 0).withNano(123));
        logger.debug("" + flowMonitor.getExpireSecond(localDateTime));
        localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 3, 0).withNano(123));
        logger.debug("" + flowMonitor.getExpireSecond(localDateTime));
        localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 2, 59).withNano(123));
        logger.debug("" + flowMonitor.getExpireSecond(localDateTime));
    }

    @Test
    public void test2() throws InterruptedException {

        for (int i = 0; i <= 100; ++i) {
            logger.debug("" + flowMonitor.getExpireSecond());
            int ra = new Random().nextInt(10);
            TimeUnit.SECONDS.sleep(ra);
        }
    }

    @Test
    public void test3() {
        LocalDateTime localDateTime = LocalDateTime.now();

        int localTime = LocalTime.now().getMinute();
        System.out.println("+++ " + localTime);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println("--- " + dtf.format(localDateTime));
        System.out.println("--- " + dtf2.format(localDateTime));
    }
}
