package com.xyzla;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = MailApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@EnableAutoConfiguration
@EnableScheduling
public class MailSenderTest {
    private static final Logger logger = LoggerFactory.getLogger(MailSenderTest.class);



}
