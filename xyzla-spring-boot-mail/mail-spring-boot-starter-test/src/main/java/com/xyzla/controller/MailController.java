package com.xyzla.controller;

import com.xyzla.mail.MailHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("mail")
public class MailController {
    private static final Logger logger = LoggerFactory.getLogger(MailController.class);

    @Autowired
    MailHelper mailHelper;

    @RequestMapping("sendTemplateMail")
    public void sendTemplateMail() {
        String subject = "[No-reply]通讯运营管理平台-账号注册成功通知";
        String tmplate = "customRegisterSuccessNotifyToUser.html";
        String sendTo = "";

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("name", "FengLin Li");
        model.put("username", "李琳锋");
        model.put("password", "test@2021");
        model.put("year", LocalDateTime.now().getYear());

        mailHelper.sendTemplateMail(sendTo, subject, tmplate, model);
    }


    @RequestMapping("sendHtmlMail")
    public void sendHtmlMail() {
        String subject = "[No-reply]通讯运营管理平台-账号注册成功通知";
        String tmplate = "customRegisterSuccessNotifyToUser.html";
        String sendTo = "";

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("name", "FengLin Li");
        model.put("username", "Ryan");
        model.put("password", "test@2021");
        model.put("year", LocalDateTime.now().getYear());

        String[] toWho = new String[]{"xqghp@126.com", "desmo@showmac.com", "ryan@showmac.com"};

        mailHelper.sendHtmlMail(subject, tmplate, model, toWho);
    }

    @RequestMapping("sendSimpleMail")
    public void sendSimpleMail() {
        String subject = "注册成功";
        String content = "Hi,欢迎您注册xxx!";
        String sendTo = "";

        mailHelper.sendSimpleMail(sendTo, subject, content);
    }

    @RequestMapping("sendAttachmentMail")
    public void sendAttachmentMail() {
        String subject = "[NoReploy][xx]管理平台注册成功";
        String content = "欢迎您注册成功<p/>请查收附件";
        String sendTo = "";

        Map<String, File> fileMap = new HashMap<>();

        fileMap.put("pushApiJavaSample.zip", new File("/home/ryan/o/pushApiJavaSample.zip"));
        fileMap.put("promtail-local-config.yaml", new File("/home/ryan/o/promtail-local-config.yaml"));

        mailHelper.sendAttachmentMail(sendTo, subject, content, fileMap);
    }

}
