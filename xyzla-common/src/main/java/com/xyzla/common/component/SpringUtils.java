package com.xyzla.common.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtils implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(SpringUtils.class);

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtils.applicationContext == null) {
            SpringUtils.applicationContext = applicationContext;
        }

        logger.info("ApplicationContext 配置成功, 在普通类可以通过调用 SpringUtils.getApplicationContext() 获取 applicationContext 对象, applicationContext = {}", SpringUtils.applicationContext);
    }

    // 获取 applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    // 通过 name 获取 Bean
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    // 通过 class 获取 Bean
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    // 通过 name,以及 Clazz 返回指定的 Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }


    // 获取当前环境
    public static String getActiveProfile() {
        return applicationContext.getEnvironment().getActiveProfiles()[0];
    }
}
