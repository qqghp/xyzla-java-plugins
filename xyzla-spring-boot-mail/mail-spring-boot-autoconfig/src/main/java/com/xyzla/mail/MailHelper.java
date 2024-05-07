package com.xyzla.mail;

import com.sun.mail.smtp.SMTPAddressFailedException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.SendFailedException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class MailHelper {

    private Logger logger = LoggerFactory.getLogger(MailHelper.class);

    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;


    @Value("${mail.username}")
    private String fromWho;
    private String nickname;

    public String getNickname() {
        return fromWho.substring(0, fromWho.indexOf("@"));
    }

    @Async
    public void sendTemplateMail(String sendTo, String subject, String tmplate, Map<String, Object> model) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            helper.setFrom(fromWho);
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

    public void sendHtmlMail(String subject, String tmplate, Map<String, Object> model, String[] toWho) {
        //检验参数：邮件主题、收件人、邮件内容必须不为空才能够保证基本的逻辑执行
        if (subject == null || toWho == null || toWho.length == 0 || tmplate == null) {
            logger.error("邮件-> {} 无法继续执行，因为缺少基本的参数：邮件主题、收件人、邮件内容", subject);
            throw new RuntimeException("模板邮件无法继续发送，因为缺少必要的参数！");
        }

        logger.info("开始发送Html邮件：主题->{}，收件人->{}", subject, toWho);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(tmplate);
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            handleBasicInfo(helper, subject, text, toWho);

            javaMailSender.send(mimeMessage);
            logger.info("send success a html Mail to {}", toWho);
        } catch (SMTPAddressFailedException ex) {
            Set<String> tmpInvalidMails = getInvalidAddress(ex);
            // 非无效收件人导致的异常，暂不处理
            if (tmpInvalidMails.isEmpty()) {
                logger.error(ex.getMessage());
                return;
            }
            List<String> mailList = Arrays.asList(toWho);
            mailList.removeAll(tmpInvalidMails);
            if (mailList.isEmpty()) {
                logger.error("邮件发送失败，无收件人" + ex.getMessage());
                return;
            }
            sendHtmlMail(subject, tmplate, model, mailList.toArray(new String[0]));
        } catch (Exception ex) {
            logger.error("HTML 邮件发送失败", ex);
        }

    }

    private Set<String> getInvalidAddress(SMTPAddressFailedException e) {
        Set<String> mails = new HashSet<>();
        for (int i = 0; i < e.getInvalidAddresses().length; i++) {
            Address address = e.getInvalidAddresses()[i];
            mails.add(address.toString());
        }
        return mails;
    }

    public void handleBasicInfo(SimpleMailMessage simpleMailMessage, String subject, String content, String[] toWho, String[] ccPeoples, String[] bccPeoples) {
        //设置发件人
        simpleMailMessage.setFrom(getNickname() + '<' + fromWho + '>');
        //设置邮件的主题
        simpleMailMessage.setSubject(subject);
        //设置邮件的内容
        simpleMailMessage.setText(content);
        //设置邮件的收件人
        simpleMailMessage.setTo(toWho);
        //设置邮件的抄送人
        simpleMailMessage.setCc(ccPeoples);
        //设置邮件的密送人
        simpleMailMessage.setBcc(bccPeoples);
    }

    public void handleBasicInfo(MimeMessageHelper mimeMessageHelper, String subject, String content, String[] toWho) {
        try {
            //设置发件人
            mimeMessageHelper.setFrom(getNickname() + '<' + fromWho + '>');
            //设置邮件的主题
            mimeMessageHelper.setSubject(subject);
            //设置邮件的内容
            mimeMessageHelper.setText(content, true);
            //设置邮件的收件人
            mimeMessageHelper.setTo(toWho);
        } catch (MessagingException e) {
            logger.error("html邮件基本信息出错->{}", subject);
        }
    }

    @Async
    public void sendSimpleMail(String sendTo, String subject, String content) {
        try {
            final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            message.setFrom(fromWho);
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
    public void sendAttachmentMail(String sendTo, String subject, String content, Map<String, File> attachement) {
        try {
            final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            message.setFrom(fromWho);

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
