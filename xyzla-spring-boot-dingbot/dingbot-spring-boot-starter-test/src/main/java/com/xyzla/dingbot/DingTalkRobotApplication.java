package com.xyzla.dingbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/** DingTalk机器人的演示启动类 */
@SpringBootApplication
@EnableScheduling
public class DingTalkRobotApplication {

    public static void main(String[] args) {
        SpringApplication.run(DingTalkRobotApplication.class, args);
    }
}
