package com.xyzla.plugins.dingbot;

import com.xyzla.plugins.dingbot.client.DingbotClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DingBotAutoConfiguration {

    private static Logger logger = LoggerFactory.getLogger(DingBotAutoConfiguration.class);

    @Bean("dingbotClient")
    public DingbotClient dingbotClient(@Value("${dingbot.prefix}") String urlPrefix,
                                       @Value("${dingbot.access-token}") String accessToken,
                                       @Value("${dingbot.secret.secret-enabled:false}") Boolean secretEnable,
                                       @Value("${dingbot.secret.secret-token}") String secretToken) {
        logger.info("--- dingbotClient initing...");
        return new DingbotClient(urlPrefix, accessToken, secretEnable, secretToken);
    }

}
