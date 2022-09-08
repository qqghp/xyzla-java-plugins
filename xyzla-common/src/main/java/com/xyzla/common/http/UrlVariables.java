package com.xyzla.common.http;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlVariables {

    public static String replace(String str, List<String> urlVariables) {
        int i = 0;
        String pattern = "\\{[^}]*\\}";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        while (m.find()) {
            String g = m.group();
            String target = g.substring(1, g.length() - 1);//去掉花括号
            System.out.println(g + "   " + target);
            str = str.replace(g, (String) urlVariables.get(i++));
        }

        return str;
    }
}
