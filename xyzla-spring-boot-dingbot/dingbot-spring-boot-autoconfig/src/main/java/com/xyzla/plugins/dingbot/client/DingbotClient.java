package com.xyzla.plugins.dingbot.client;

import com.xyzla.plugins.dingbot.entity.*;
import com.xyzla.plugins.dingbot.exception.DingTalkException;
import com.xyzla.plugins.dingbot.type.HideAvatarType;
import com.xyzla.plugins.dingbot.type.ResponseCodeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * 钉钉机器人客户端
 */
public class DingbotClient {
    private static final Logger log = LoggerFactory.getLogger(DingbotClient.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 钉钉机器人 WebHook 地址的默认前缀
     */
    private String urlPrefix;
    /**
     * 钉钉机器人 WebHook 地址的 access_token
     */
    private String accessToken;

    /**
     * 钉钉机器人是否启用加签，默认 false，启用加签需设置 secret_token
     */
    private boolean secretEnable;

    /**
     * 钉钉机器人 WebHook 地址的 secret_token,群机器人加签用
     */
    private String secretToken;


    private DingbotClient() {

    }

    public DingbotClient(String urlPrefix, String accessToken, Boolean secretEnable, String secretToken) {
        this.urlPrefix = urlPrefix;
        this.accessToken = accessToken;
        this.secretEnable = secretEnable;
        this.secretToken = secretToken;
    }

    /**
     * Gets default webhook.
     *
     * @return the default webhook
     */
    public String getDefaultWebhook() {
        return getWebhook(accessToken, secretToken, secretEnable);
    }

    /**
     * Gets webhook. 隐含条件，加签功能关闭 传入accessToken，获取完整的WebHook URL。
     *
     * @param accessToken the access token
     * @return the webhook
     */
    public String getWebhook(String accessToken) {
        return getWebhook(accessToken, null, false);
    }

    /**
     * 计算签名 参考：https://ding-doc.dingtalk.com/doc#/serverapi2/qf2nxq/9e91d73c
     *
     * @param secret    密钥，机器人安全设置页面，加签一栏下面显示的SEC开头的字符
     * @param timestamp 当前时间戳，毫秒级单位
     * @return 根据时间戳计算后的签名信息
     */
    private static String getSign(String secret, Long timestamp) {
        try {
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            // String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
            String sign = URLEncoder.encode(new String(Base64.getEncoder().encodeToString(signData)), "UTF-8");
            log.debug("【发送钉钉群消息】获取到签名sign = {}", sign);
            return sign;
        } catch (Exception e) {
            log.error("【发送钉钉群消息】计算签名异常，errMsg = {}", e);
            return null;
        }
    }

    /**
     * 支持外部传入WebHook地址的方式发送消息
     *
     * @param url
     * @param message
     * @return
     */
    public DingTalkResponse sendMessageByURL(String url, BaseMessage message) {
        URI actualUrl;
        try {
            actualUrl = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URL创建报错！");
        }
        return sendMessageByURL(actualUrl, message);
    }

    /**
     * 支持外部传入WebHook地址的方式发送消息
     *
     * @param url
     * @param message
     * @return
     */
    public DingTalkResponse sendMessageByURL(URI url, BaseMessage message) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<>(message.toMessageMap(), headers);
        DingTalkResponse dingTalkResponse;

        try {
            dingTalkResponse = restTemplate.postForObject(url, entity, DingTalkResponse.class);
        } catch (Exception e) {
            log.error(e.fillInStackTrace().toString());
            throw new DingTalkException(
                    ResponseCodeType.UNKNOWN.getValue(), e.fillInStackTrace().toString());
        }

        // 对DingTalkResponse为空情况做异常封装
        if (dingTalkResponse == null) {
            throw new DingTalkException(ResponseCodeType.UNKNOWN.getValue(), "请求钉钉报错！");
        }
        if (!ResponseCodeType.OK.getValue().equals(dingTalkResponse.getErrcode())) {
            throw new DingTalkException(
                    dingTalkResponse.getErrcode(), dingTalkResponse.getErrmsg());
        }
        return dingTalkResponse;
    }

    /**
     * 支持外部传入AccessToken地址的方式发送消息
     *
     * @param accessToken
     * @param message
     * @return
     */
    public DingTalkResponse sendMessageByAccessToken(String accessToken, BaseMessage message) {
        return this.sendMessageByURL(getWebhook(accessToken), message);
    }

    /**
     * 发送WebHook消息到钉钉
     *
     * @param message
     * @return
     */
    public DingTalkResponse sendMessage(BaseMessage message) {
        return this.sendMessageByURL(getDefaultWebhook(), message);
    }

    /**
     * 发送文本消息到钉钉
     *
     * @param message
     * @return
     */
    public DingTalkResponse sendTextMessage(TextMessage message) {
        return this.sendMessage(message);
    }

    /**
     * 发送文本消息到钉钉
     *
     * @param content
     * @return
     */
    public DingTalkResponse sendTextMessage(String content) {
        return this.sendMessage(new TextMessage(content));
    }

    /**
     * 发送文本消息到钉钉
     *
     * @param content
     * @param atMobiles
     * @return
     */
    public DingTalkResponse sendTextMessage(String content, String[] atMobiles) {
        return this.sendMessage(new TextMessage(content, atMobiles));
    }

    /**
     * 发送文本消息到钉钉
     *
     * @param content
     * @param isAtAll
     * @return
     */
    public DingTalkResponse sendTextMessage(String content, boolean isAtAll) {
        return this.sendMessage(new TextMessage(content, isAtAll));
    }

    /**
     * 发送Link消息到钉钉
     *
     * @param message
     * @return
     */
    public DingTalkResponse sendLinkMessage(LinkMessage message) {
        return this.sendMessage(message);
    }

    /**
     * 发送Link消息到钉钉
     *
     * @param title
     * @param text
     * @param messageUrl
     * @return
     */
    public DingTalkResponse sendLinkMessage(String title, String text, String messageUrl) {
        return this.sendMessage(new LinkMessage(title, text, messageUrl));
    }

    /**
     * 发送Link消息到钉钉
     *
     * @param title
     * @param text
     * @param messageUrl
     * @param picUrl
     * @return
     */
    public DingTalkResponse sendLinkMessage(
            String title, String text, String messageUrl, String picUrl) {
        return this.sendMessage(new LinkMessage(title, text, messageUrl, picUrl));
    }

    /**
     * 发送MarkDown消息到钉钉
     *
     * @param message
     * @return
     */
    public DingTalkResponse sendMarkdownMessage(MarkdownMessage message) {
        return this.sendMessage(message);
    }

    /**
     * 发送MarkDown消息到钉钉
     *
     * @param title
     * @param text
     * @return
     */
    public DingTalkResponse sendMarkdownMessage(String title, String text) {
        return this.sendMessage(new MarkdownMessage(title, text));
    }

    /**
     * 发送MarkDown消息到钉钉
     *
     * @param title
     * @param text
     * @param atMobiles
     * @return
     */
    public DingTalkResponse sendMarkdownMessage(String title, String text, String[] atMobiles) {
        return this.sendMessage(new MarkdownMessage(title, text, atMobiles));
    }

    /**
     * 发送MarkDown消息到钉钉
     *
     * @param title
     * @param text
     * @param isAtAll
     * @return
     */
    public DingTalkResponse sendMarkdownMessage(String title, String text, boolean isAtAll) {
        return this.sendMessage(new MarkdownMessage(title, text, isAtAll));
    }

    /**
     * 发送ActionCard消息到钉钉
     *
     * @param message
     * @return
     */
    public DingTalkResponse sendActionCardMessage(ActionCardMessage message) {
        return this.sendMessage(message);
    }

    /**
     * 发送ActionCard消息到钉钉
     *
     * @param title
     * @param text
     * @return
     */
    public DingTalkResponse sendActionCardMessage(String title, String text) {
        return this.sendMessage(new ActionCardMessage(title, text));
    }

    /**
     * 发送ActionCard消息到钉钉
     *
     * @param title
     * @param text
     * @param hideAvatar
     * @return
     */
    public DingTalkResponse sendActionCardMessage(
            String title, String text, HideAvatarType hideAvatar) {
        return this.sendMessage(new ActionCardMessage(title, text, hideAvatar));
    }

    /**
     * 发送ActionCard消息到钉钉
     *
     * @param title
     * @param text
     * @param button
     * @return
     */
    public DingTalkResponse sendActionCardMessage(
            String title, String text, ActionCardButton button) {
        return this.sendMessage(new ActionCardMessage(title, text, button));
    }

    /**
     * 发送ActionCard消息到钉钉
     *
     * @param title
     * @param text
     * @param hideAvatar
     * @param button
     * @return
     */
    public DingTalkResponse sendActionCardMessage(
            String title, String text, HideAvatarType hideAvatar, ActionCardButton button) {
        return this.sendMessage(new ActionCardMessage(title, text, hideAvatar, button));
    }

    /**
     * 发送FeedCard消息到钉钉
     *
     * @param feedCardMessage
     * @return
     */
    public DingTalkResponse sendFeedCardMessage(FeedCardMessage feedCardMessage) {
        return this.sendMessage(feedCardMessage);
    }

    /**
     * 发送FeedCard消息到钉钉
     *
     * @param feedCardItems
     * @return
     */
    public DingTalkResponse sendFeedCardMessage(List<FeedCardMessageItem> feedCardItems) {
        return this.sendMessage(new FeedCardMessage(feedCardItems));
    }

    /**
     * Gets webhook. access_token、secret_token、secretEnable参数均由外部传入
     *
     * @param accessToken the access token
     * @return the webhook
     */
    public String getWebhook(String accessToken, String secretToken, boolean secretEnable) {
        String url = urlPrefix + "?access_token=" + accessToken;
        // 若启用加签加上时间戳跟签名串
        if (secretEnable) {
            Long timestamp = System.currentTimeMillis();
            url += "&timestamp=" + timestamp + "&sign=" + getSign(secretToken, timestamp);
        }
        log.debug("The url contains sign is {}", url);
        return url;
    }

    /**
     * Gets webhook. 隐含条件，加签功能开启 传入accessToken和secretToken，获取完整的WebHook URL。
     *
     * @param accessToken
     * @param secretToken
     * @return
     */
    public String getWebhook(String accessToken, String secretToken) {
        return getWebhook(accessToken, secretToken, true);
    }
}
