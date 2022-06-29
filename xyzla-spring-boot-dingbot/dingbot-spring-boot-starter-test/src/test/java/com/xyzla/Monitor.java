package com.xyzla;

import com.xyzla.dingbot.DingTalkRobotApplication;
import com.xyzla.dingbot.monitor.FlowMonitor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = DingTalkRobotApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@EnableAutoConfiguration
@EnableScheduling
public class Monitor {
    private Logger log = LoggerFactory.getLogger(Monitor.class);
    @Autowired
    private FlowMonitor flowMonitor;

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void queryCardFlow() throws InterruptedException {
        flowMonitor.dailyAlarm("Z3611212304", 1L );
        flowMonitor.dailyAlarm("A13611212305",1048576L);
        flowMonitor.dailyAlarm("B13611212305",2048577L);
        flowMonitor.dailyAlarm("C13611212305",3048578l);
        flowMonitor.dailyAlarm("D13611212305",4048579l);

        flowMonitor.dailyAlarm("E13611212305",1L );
        flowMonitor.dailyAlarm("F13611212305",31457280L);
        flowMonitor.dailyAlarm("G13611212305",41457281L);
        flowMonitor.dailyAlarm("H13611212305",32457282L);
        flowMonitor.dailyAlarm("I13611212305",33457283L);


        TimeUnit.SECONDS.sleep(30);
    }
}
