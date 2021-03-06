package com.xyzla.common.util;

public class UnicodeUtil {

    //Unicode转中文方法
    public static String encoding(String unicode) {
        /** 以 \ u 分割，因为 java 注释也能识别 unicode，因此中间加了一个空格 */
        String[] strs = unicode.split("\\\\u");

        String returnStr = "";
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (int i = 1; i < strs.length; i++) {
            returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
        }
        return returnStr;
    }

    //中文转Unicode
    public static String decodeUnicode(String cn) {
        char[] chars = cn.toCharArray();
        String returnStr = "";
        for (int i = 0; i < chars.length; i++) {
            returnStr += "\\u" + Integer.toString(chars[i], 16);
        }
        return returnStr;
    }

    //测试
    private static void main(String[] args) {
        //Unicode码
        String aa = "\\u5916\\u56fd\\u4eba\\u88ab\\u4e2d\\u56fd\\u8fd9\\u4e00\\u5927\\u52a8\\u4f5c\\u6298\\u670d";
        //转中文
        String cnAa = encoding(aa);
        System.out.println("Unicode转中文结果： " + cnAa);//转Unicode
        String unicodeAa = decodeUnicode(cnAa);
        System.out.println("中文转Unicode结果： " + unicodeAa);
    }
}
