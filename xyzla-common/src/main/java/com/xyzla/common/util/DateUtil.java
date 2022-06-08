package com.xyzla.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        pattern = pattern == null ? "yyyy-MM-dd" : pattern;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }


    public static Date parse(String strDate, String pattern) {
        if (strDate == null) {
            return null;
        }
        pattern = pattern == null ? "yyyyMMdd" : pattern;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(strDate);
        } catch (ParseException parseException) {
            return null;
        }
    }


}