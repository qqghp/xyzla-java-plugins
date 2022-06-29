package com.xyzla;

import com.xyzla.dingbot.DingTalkRobotApplication;
import com.xyzla.plugins.dingbot.client.DingbotClient;
import com.xyzla.plugins.dingbot.entity.*;
import com.xyzla.plugins.dingbot.type.HideAvatarType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

@SpringBootTest(classes = DingTalkRobotApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
public class DingTalkRobotApplicationTests {

    private Logger log = LoggerFactory.getLogger(DingTalkRobotApplicationTests.class);

    @Autowired
    @Qualifier("dingbotClient")
    private DingbotClient client;

    /**
     * 测试 DingTalkRobotClient#sendMessage(BaseMessage) 发送消息
     *
     * @throws InterruptedException
     * @see DingbotClient#sendMessage(BaseMessage)
     */
    @Test
    public void testSendMessageWithText() throws InterruptedException {
        DingTalkResponse response = null;

        response = client.sendMessage(new TextMessage("Info.\nHelloWorld!"));
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

        response = client.sendMessage(new LinkMessage("Info.\n习近平等党和国家领导人出席全国政协十三届一次会议闭幕会",
                "全国政协十三届一次会议闭幕 习近平李克强张德江俞正声张高丽栗战书王沪宁赵乐际韩正出席 汪洋发表讲话",
                "http://mp.weixin.qq.com/s/UfmtYLSZL7kgCNnKC6Co0Q",
                "http://www.scps.gov.cn/images/17/03/02/1our9zwzfg/C6065233104C46BBC626EB4FC48A4941.jpg"));
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

        response = client.sendMessage(new MarkdownMessage("HelloWorld 钉钉markdown消息支持的语法",
                markDownDemoText));

        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

        response = client.sendMessage(new ActionCardMessage("Info This is title", "![screenshot](@lADOpwk3K80C0M0FoA)\n" +
                "**Apple Store** 的设计正从原来满满的科技感走向生活化，而其生活化的走向其实可以追溯到 20 年前苹果一个建立咖啡馆的计划",
                HideAvatarType.HIDE));
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

    }

    /**
     * 测试 DingTalkRobotClient#sendTextMessage() 发送文本消息
     *
     * @throws InterruptedException
     * @see DingbotClient#sendTextMessage(String)
     */
    @Test
    public void testSendTextMessage() throws InterruptedException {
        DingTalkResponse response = null;

        response = client.sendTextMessage(new TextMessage("Info!"));
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

        response = client.sendTextMessage("Info!");
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

        response = client.sendTextMessage("Info!", new String[]{"13123456789"});
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

        response = client.sendTextMessage("Info!", true);
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);
    }

    /**
     * 测试 DingTalkRobotClient#sendLinkMessage() 发送链接消息
     *
     * @throws InterruptedException
     */
    @Test
    public void testSendLinkMessage() throws InterruptedException {
        DingTalkResponse response = null;

        response = client.sendLinkMessage(new LinkMessage("Info习近平等党和国家领导人出席全国政协十三届一次会议闭幕会",
                "全国政协十三届一次会议闭幕 习近平李克强张德江俞正声张高丽栗战书王沪宁赵乐际韩正出席 汪洋发表讲话",
                "http://mp.weixin.qq.com/s/UfmtYLSZL7kgCNnKC6Co0Q",
                "http://www.scps.gov.cn/images/17/03/02/1our9zwzfg/C6065233104C46BBC626EB4FC48A4941.jpg"));
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

        response = client.sendLinkMessage("Info习近平等党和国家领导人出席全国政协十三届一次会议闭幕会",
                "全国政协十三届一次会议闭幕 习近平李克强张德江俞正声张高丽栗战书王沪宁赵乐际韩正出席 汪洋发表讲话",
                "http://mp.weixin.qq.com/s/UfmtYLSZL7kgCNnKC6Co0Q");
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

        response = client.sendLinkMessage("Info习近平等党和国家领导人出席全国政协十三届一次会议闭幕会",
                "全国政协十三届一次会议闭幕 习近平李克强张德江俞正声张高丽栗战书王沪宁赵乐际韩正出席 汪洋发表讲话",
                "http://mp.weixin.qq.com/s/UfmtYLSZL7kgCNnKC6Co0Q",
                "http://www.scps.gov.cn/images/17/03/02/1our9zwzfg/C6065233104C46BBC626EB4FC48A4941.jpg");
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

    }

    /**
     * 测试 DingTalkRobotClient#sendMarkdownMessage() 发送链接消息
     *
     * @throws InterruptedException
     */
    @Test
    public void testSendMarkdownMessage() throws InterruptedException {
        DingTalkResponse response = null;

        response = client.sendMarkdownMessage(new MarkdownMessage("Info钉钉markdown消息支持的语法",
                markDownDemoText));
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

        response = client.sendMarkdownMessage("Info钉钉markdown消息支持的语法",
                markDownDemoText);
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

        response = client.sendMarkdownMessage("Info钉钉markdown消息支持的语法",
                markDownDemoText, new String[]{"13123456789"});
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

        response = client.sendMarkdownMessage("钉钉markdown消息支持的语法",
                markDownDemoText, true);
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);
    }

    /**
     * 测试 DingTalkRobotClient#sendActionCardMessage() 发送ActionCard消息
     *
     * @throws InterruptedException
     */
    @Test
    public void testSendActionCardMessage() throws InterruptedException {
        DingTalkResponse response = null;

        response = client.sendActionCardMessage(new ActionCardMessage("Info This is title", "![screenshot](@lADOpwk3K80C0M0FoA)\n" +
                "**Apple Store** 的设计正从原来满满的科技感走向生活化，而其生活化的走向其实可以追溯到 20 年前苹果一个建立咖啡馆的计划"));
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

        response = client.sendActionCardMessage("Info This is title", "![screenshot](@lADOpwk3K80C0M0FoA)\n" +
                "**Apple Store** 的设计正从原来满满的科技感走向生活化，而其生活化的走向其实可以追溯到 20 年前苹果一个建立咖啡馆的计划");
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

        response = client.sendActionCardMessage("Info This is title", "![screenshot](@lADOpwk3K80C0M0FoA)\n" +
                        "**Apple Store** 的设计正从原来满满的科技感走向生活化，而其生活化的走向其实可以追溯到 20 年前苹果一个建立咖啡馆的计划",
                HideAvatarType.HIDE);
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

        response = client.sendActionCardMessage("Info This is title", "![screenshot](@lADOpwk3K80C0M0FoA)\n" +
                        "**Apple Store** 的设计正从原来满满的科技感走向生活化，而其生活化的走向其实可以追溯到 20 年前苹果一个建立咖啡馆的计划",
                ActionCardButton.defaultReadButton("https://www.dingtalk.com"));
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

        response = client.sendActionCardMessage("Info This is title", "![screenshot](@lADOpwk3K80C0M0FoA)\n" +
                        "**Apple Store** 的设计正从原来满满的科技感走向生活化，而其生活化的走向其实可以追溯到 20 年前苹果一个建立咖啡馆的计划",
                HideAvatarType.HIDE,
                ActionCardButton.defaultReadButton("https://www.dingtalk.com"));
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

    }

    /**
     * 测试 DingTalkRobotClient#sendFeedCardMessage() 发送FeedCard消息
     *
     * @throws InterruptedException
     */
    @Test
    public void testSendFeedCardMessage() throws InterruptedException {
        DingTalkResponse response = null;

        ArrayList<FeedCardMessageItem> items = new ArrayList<>();
        items.add(new FeedCardMessageItem("Info 成为架构师的路上，看这一篇文章就足够了，因为……",
                "http://mp.weixin.qq.com/s/CPUaB60pue0Zf3fUL9xqvw",
                "https://mmbiz.qpic.cn/mmbiz_jpg/YriaiaJPb26VMtfgPvTsM9amH5hf3pmTbf40ia6OLE845icrDb0vt4AsMnTyva5mMMpwwxnkVR5UjCEI8ADvSic1qWQ/640"));

        items.add(new FeedCardMessageItem("Info 想成为一名Web开发者？你应该学习Node.js而不是PHP",
                "http://mp.weixin.qq.com/s/x8dm9e7gwLXSEzxE6sQYow",
                "https://mmbiz.qpic.cn/mmbiz_jpg/YriaiaJPb26VND0Q0hSBOoyVkr9cXQrFjWI7hOzax1IxIibqanXYD4W8nyeYX5iaicjgiaqia7ly94iawOsGwehbKGwGsA/640"));

        response = client.sendFeedCardMessage(new FeedCardMessage(items));
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);

        response = client.sendFeedCardMessage(items);
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        log.info(response.toString());
        Thread.sleep(3000);
    }

    /**
     * 通过调用getWebhook方法来做外部的配置项传入，之后再进行消息发送
     */
    @Test
    public void testGetWebhookAndSendMessage() throws InterruptedException {
        DingTalkResponse response;

        TextMessage message = new TextMessage("HelloWorld!");

        String nonSignAccessToken = "change-to-your-access-token";
        String signAccessToken = "change-to-your-access-token";
        String signSecretToken = "change-to-your-secret-token";

        String actualUrl;

        actualUrl = client.getWebhook(nonSignAccessToken);
        response = client.sendMessageByURL(actualUrl, message);
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        Thread.sleep(3000);

        actualUrl = client.getWebhook(nonSignAccessToken, null, false);
        response = client.sendMessageByURL(actualUrl, message);
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        Thread.sleep(3000);

        actualUrl = client.getWebhook(signAccessToken, signSecretToken);
        response = client.sendMessageByURL(actualUrl, message);
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        Thread.sleep(3000);

        actualUrl = client.getWebhook(signAccessToken, signSecretToken, true);
        response = client.sendMessageByURL(actualUrl, message);
        Assert.assertEquals(response.getErrcode().longValue(), 0L);
        Thread.sleep(3000);
    }

    private static final String markDownDemoText = "标题\n" +
            "# 一级标题\n" +
            "## 二级标题\n" +
            "### 三级标题\n" +
            "#### 四级标题\n" +
            "##### 五级标题\n" +
            "###### 六级标题\n" +
            " \n" +
            "引用\n" +
            "> A man who stands for nothing will fall for anything.\n" +
            " \n" +
            "文字加粗、斜体\n" +
            "**bold**\n" +
            "*italic*\n" +
            " \n" +
            "链接\n" +
            "[阿里钉钉](https://www.dingtalk.com/)\n" +
            " \n" +
            "图片\n" +
            "![](https://pic1.znj.com/Uploads/Editor/2018-02-06/5a7955fddf7ad.png)\n" +
            " \n" +
            "无序列表\n" +
            "- item1\n" +
            "- item2\n" +
            " \n" +
            "有序列表\n" +
            "1. item1\n" +
            "2. item2";

}
