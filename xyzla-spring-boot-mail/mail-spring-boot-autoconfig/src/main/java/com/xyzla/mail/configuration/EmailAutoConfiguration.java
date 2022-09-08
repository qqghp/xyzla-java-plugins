package com.xyzla.mail.configuration;

import com.xyzla.mail.MailHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(EmailAutoConfiguration.class);

    @Bean
    public JavaMailSender javaMailSender(@Value("${mail.host}") String mailHost,
                                         @Value("${mail.port}") Integer mailPort,
                                         @Value("${mail.username}") String mailUsername,
                                         @Value("${mail.password}") String mailPassword) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setDefaultEncoding("utf-8");

        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);

        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }


    @Bean
    public MailHelper mailHelper() {
        logger.info("Mail Helper 初始化 ...");
        return new MailHelper();
    }

//    @Bean
//    public SimpleMailMessage emailTemplate() {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo("somebody@gmail.com");
//        message.setFrom("admin@gmail.com");
//        message.setText("FATAL - Application crash. Save your job !!");
//        return message;
//    }
}
