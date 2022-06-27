package com.showmac.controller;

import com.showmac.entity.SmsMessageEntity;
import com.showmac.entity.TopicDemoEntity;
import com.xyzla.mq.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {

    @Autowired
    Producer producer;

    private static final String TOPIC = "SMS";

    @RequestMapping("send")
    public void send() {
        for (int i = 0; i < 10; i++) {
            String message = "message-" + i;
            producer.send(TOPIC, message);
            producer.send("SMS_RECORD", new SmsMessageEntity(i + "", message));
            producer.send("TOPIC_DEMO", new TopicDemoEntity(i + "", message));
        }
    }
}
