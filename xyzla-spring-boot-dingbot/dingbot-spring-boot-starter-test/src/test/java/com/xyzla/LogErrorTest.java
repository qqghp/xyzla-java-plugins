package com.xyzla;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Log4j打印错误异常的详细堆栈信息
// https://www.jianshu.com/p/fb31b91558b6
public class LogErrorTest {

    private static final Logger logger = LoggerFactory.getLogger(LogErrorTest.class);

    public static void main(String[] args) {
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            logger.error("使用 + 号连接直接输出 e ：" + e);
            System.out.println("--------------------------------------------");
            logger.error("使用 + 号连接直接输出 e.toString() ：" + e.toString());
            System.out.println("--------------------------------------------");
            logger.error("使用 + 号连接直接输出 e.getMessage() ：" + e.getMessage());
            System.out.println("--------------------------------------------");
            logger.error("使用 ，号 且第二个参数为Throwable :", e);
            System.out.println("--------------------------------------------");
            logger.error("第二个参数为Throwable,使用分隔符 {} :", e);
            System.out.println("--------------------------------------------");
            logger.error("第二个参数为Object,使用分隔符{} :", "AAA");
            System.out.println("--------------------------------------------");
            logger.error("{}", e);
            System.out.println("--------------------------------------------");
            logger.error("{}", e.getMessage());
        }
    }
}
