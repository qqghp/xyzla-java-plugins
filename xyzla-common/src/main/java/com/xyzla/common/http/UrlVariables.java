package com.xyzla.common.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlVariables {
    private static final Logger logger = LoggerFactory.getLogger(UrlVariables.class);

    public static String replace(String str, List<String> urlVariables) {
        int i = 0;
        String pattern = "\\{[^}]*\\}";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        while (m.find()) {
            String g = m.group();
            String target = g.substring(1, g.length() - 1);//去掉花括号
            if (logger.isDebugEnabled()) {
                logger.debug("{} -> {}", g, target);
            }
            str = str.replace(g, (String) urlVariables.get(i++));
        }

        return str;
    }
}
