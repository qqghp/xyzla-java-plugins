package com.xyzla;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://www.cnblogs.com/zhang741741/p/16145400.html
 */
public class Test2 {

    public static String replace(String msg, Object... params) {
        if (msg == null) {
            throw new NullPointerException("msg");
        }
        StringBuffer sb = new StringBuffer();
        //定界符
        final String delimiter = "{}";
        //括号出现的计数值
        int cnt = 0;
        if (params != null && params.length > 0) {
            for (int i = 0; i <= params.length; i++) {
                int tmpIndex = msg.indexOf(delimiter);
                //不存在赋值
                if (tmpIndex == -1) {
                    if (cnt == 0 || StringUtils.isNotBlank(msg)) {
                        sb.append(msg);
                    }
                    break;
                } else {
                    //存在则进行赋值拼接
                    String str = msg.substring(0, tmpIndex);
                    msg = msg.substring((tmpIndex + 2), msg.length());
                    String valStr = params[i].toString();
                    sb.append(str).append(valStr);
                    cnt++;

                }
            }
        } else {//param为空时
            sb.append(msg);
        }
        return sb.toString();
    }

    public static String replace2(String data) {
        data = Pattern.compile("(?<=\\{).*?(?=\\})", Pattern.DOTALL).matcher(data).replaceAll("");

        return data;
//        "if (true) { \n\t\tcalc();\n }" becomes "if (true) {}"
    }

    public static String replace3(String str) {
        String pattern = "\\{[^}]*\\}";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        while (m.find()) {
            String g = m.group();
            String target = g.substring(1, g.length() - 1);//去掉花括号
            System.out.println(g + "   " + target);
            str = str.replace(g, target + " <= '2019-09-09'");
        }

        return str;
    }

    public static void main(String[] args) {
        String csd = replace("新媒体{}文章严重错敏词{}通知:{}", "aa", "bb", "cc");
        System.out.println(csd);

        System.out.println(replace("ceshi/{} cost {} millis", "你好", 123));

        System.out.println(replace2("ceshi/{你好} cost {} millis"));

        System.out.println(MessageFormat.format("你好 {0} {1}", "123", 123));

        System.out.println(replace3("{f.name} and {f.age}"));
    }
}
