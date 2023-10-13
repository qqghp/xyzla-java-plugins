package com.xyzla.mail;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class MailHelper {

    private Logger logger = LoggerFactory.getLogger(MailHelper.class);

    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Async
    public void sendTemplateMail(String sendFrom, String sendTo, String subject, String tmplate, Map<String, Object> model) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            helper.setFrom(sendFrom);
            helper.setTo(sendTo);
            helper.setSubject(subject);

            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(tmplate);
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            //html: true  代表发送HTML
            helper.setText(text, true);
            javaMailSender.send(mimeMessage);

            logger.info("send success a html Mail to {}", sendTo);
        } catch (MessagingException | IOException | TemplateException e) {
            logger.error("{}", e);
        }
    }

    @Async
    public void sendSimpleMail(String sendFrom, String sendTo, String subject, String content) {
        try {
            final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            message.setFrom(sendFrom);
            message.setTo(sendTo);
            message.setSubject(subject);

            message.setText(content);
            javaMailSender.send(mimeMessage);

            logger.info("send success a simple Mail to {}", sendTo);
        } catch (MessagingException e) {
            logger.error("{}", e);
        }
    }

    @Async
    public void sendAttachmentMail(String sendFrom, String sendTo, String subject, String content, Map<String, File> attachement) {
        try {
            final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            message.setFrom(sendFrom);

            if (sendTo.contains(",")) {
                message.setTo(sendTo.replace(" ", "").split(","));
            } else {
                message.setTo(sendTo.replace(" ", ""));
            }
            message.setSubject(subject);
            message.setText(content);

            // 抄送
            //message.setCc("");

            // 密送
            message.setBcc("xqghp@126.com");

            // for (Map.Entry<String, File> entry : attachement.entrySet()) { }
            attachement.forEach((k, v) -> {
                try {
                    message.addAttachment(MimeUtility.encodeText(k), new FileSystemResource(v));
                } catch (MessagingException | UnsupportedEncodingException e) {
                    logger.error("{}", e);
                }
            });


            javaMailSender.send(mimeMessage);
            logger.info("send success a Attachment Mail to {}", sendTo);
        } catch (MessagingException e) {
            logger.error("{}", e);
        }
    }

}
