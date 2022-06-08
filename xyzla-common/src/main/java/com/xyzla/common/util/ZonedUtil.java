package com.xyzla.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ZonedUtil {
    private static final Logger _logger = LoggerFactory.getLogger(ZonedUtil.class);

    // time zone
    // https://zh.wikipedia.org/wiki/时区列表
    public static final ZoneId zoneIdOfCST = ZoneId.of("UTC+08:00"); // 东八区   <北京>
    public static final ZoneId zoneIdOfUTC = ZoneId.of("UTC+00:00"); // UTC / GMT
    public static final ZoneId zoneIdOfMST = ZoneId.of("UTC-07:00"); // 西七区   <美国>
    public static final ZoneId zoneIdOfBHT = ZoneId.of("UTC+06:00"); // 东六区  孟加拉标准时间
    public static final ZoneId zoneIdOfIST = ZoneId.of("UTC+07:00"); // 东七区  中南半岛标准时间

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    // DateTimeFormatter 是线程安全的
    public static final DateTimeFormatter DTF_DTMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
    public static final DateTimeFormatter DTF_DTSS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DTF_DTMI = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter DTF_DTHH = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");


    public static final DateTimeFormatter DTF_DTDD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DTF_DTMO = DateTimeFormatter.ofPattern("yyyy-MM");
    public static final DateTimeFormatter DTF_DTYY = DateTimeFormatter.ofPattern("yyyy");


    public static final DateTimeFormatter DTF_TMS = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
    public static final DateTimeFormatter DTF_TSS = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter DTF_TMI = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter DTF_THH = DateTimeFormatter.ofPattern("HH");


    public static final DateTimeFormatter DTF_SIM_DTMS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss,SSS");
    public static final DateTimeFormatter DTF_SIM_DTSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter DTF_SIM_DTMI = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    public static final DateTimeFormatter DTF_SIM_DTHH = DateTimeFormatter.ofPattern("yyyyMMddHH");

    public static final DateTimeFormatter DTF_SIM_DTDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter DTF_SIM_DTMO = DateTimeFormatter.ofPattern("yyyyMM");


    public static final DateTimeFormatter DTF_DTHH_ZERO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00");
    public static final DateTimeFormatter DTF_MMDD_HHMMSS = DateTimeFormatter.ofPattern("MMddHHmmss");

    public static final DateTimeFormatter DTF_SIM_DTDD_TSS = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    public static final DateTimeFormatter DTF_SIM_DTDD_TMI = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");

    /**
     * 计算日期{@code startDate}与{@code endDate}的间隔天数
     *
     * @param startDate
     * @param endDate
     * @return 间隔天数
     */
    public static long localDateCompare(LocalDate startDate, LocalDate endDate) {
        return startDate.until(endDate, ChronoUnit.DAYS);
    }
}