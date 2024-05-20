package com.xyzla.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

public class CacheBitMapUtil {
    private static final Logger logger = LoggerFactory.getLogger(CacheBitMapUtil.class);
    private static final int SECONDS_PER_DAY = 24 * 60 * 60;
    private static final int UTC_8H = 8 * 60 * 60;

    /**
     * String 转 二进制
     *
     * @param heartBeatFreq
     * @return
     */
    public static String stringToBinary(String heartBeatFreq) {
        if (!StringUtils.hasText(heartBeatFreq)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        byte[] bytes = heartBeatFreq.getBytes(); // 获取字节数组

        for (byte b : bytes) {
            int val = b; // 将byte转换为int，避免负数导致结果错误
            for (int i = 0; i < 8; i++) {
                sb.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            sb.append(' ');
        }
        if (logger.isDebugEnabled()) {
            System.out.println(sb.toString());
        }
        return sb.toString();
    }

    public static List<String> resolve(String heartBeatFreq, ZoneOffset zoneOffset) {
        StopWatch sw = new StopWatch();
        sw.start();
        if (!StringUtils.hasText(heartBeatFreq)) {
            return Collections.emptyList();
        }

        List<String> list = new ArrayList<>();
        byte[] bytes = heartBeatFreq.getBytes();
        int secondsOfDay = 0;
        BitSet bitSet = BitSet.valueOf(bytes);
        for (int i = 0; i < bitSet.length(); i++) {
            ++secondsOfDay;
            if (bitSet.get(i)) {
                OffsetTime _time = LocalTime.ofSecondOfDay(secondsOfDay).atOffset(zoneOffset);
                String time = _time.format(ZonedUtil.DTF_TSS);
                list.add(time);

//                int hours = secondsOfDay / 3600;
//                int minutes = (secondsOfDay % 3600) / 60;
//                int remainingSeconds = secondsOfDay % 60;
//
//                String time = String.format("%d:%02d:%02d", hours, minutes, remainingSeconds);
//                list.add(time);

//                if (logger.isDebugEnabled()) {
                logger.info("resolve 产生了心跳 {} 秒 --> {}", secondsOfDay, time);
//                }
            }
        }
        sw.stop();
        if (logger.isDebugEnabled()) {
            logger.debug("resolve cost {} millis.", sw.getTotalTimeMillis());
        }
        return list;
    }

    public static List<String> parse(String heartBeatFreq) {
        StopWatch sw = new StopWatch();
        sw.start();
        if (!StringUtils.hasText(heartBeatFreq)) {
            return Collections.emptyList();
        }

        List<String> list = new ArrayList<>();
        byte[] bytes = heartBeatFreq.getBytes();
        int secondsOfDay = UTC_8H;
        for (int i = 0; i < bytes.length; i++) {
            byte currentByte = bytes[i];
            int mask = 1;
            for (int j = 0; j < 8; j++) {
//            for (int j = 8; j > 0; j--) {
                ++secondsOfDay;
                //if (((b >> j) & 0x01) == 1) {
                if ((currentByte & mask) != 0) {
                    int hours = secondsOfDay / 3600;
                    int minutes = (secondsOfDay % 3600) / 60;
                    int remainingSeconds = secondsOfDay % 60;

                    String time = String.format("%d:%02d:%02d", hours, minutes, remainingSeconds);
                    list.add(time);

                    if (logger.isDebugEnabled()) {
                        logger.debug("parse 产生了心跳 {} 秒 --> {} {}", secondsOfDay, time, mask);
                    }
                }
                mask <<= 1;
            }
        }
        sw.stop();
        if (logger.isDebugEnabled()) {
            logger.debug("parse cost {} millis.", sw.getTotalTimeMillis());
        }
        return list;
    }


    public static List<String> zzz(List<Long> list, int offset) {
        int secondsOfDay = offset;
        List<String> times = new ArrayList<>();
        for (Long l : list) {
            byte[] bytes = ByteUtil.longToBytes8(l);
            for (int i = 0; i < bytes.length; i++) {
                byte currentByte = bytes[i];
                int mask = 1;
                for (int j = 0; j < 8; j++) {
                    ++secondsOfDay;
                    if ((currentByte & mask) != 0) {
                        int hours = secondsOfDay / 3600;
                        int minutes = (secondsOfDay % 3600) / 60;
                        int remainingSeconds = secondsOfDay % 60;
                        String time = String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
                        times.add(time);

                        logger.info("zzz 产生了心跳 {} 秒 --> {} {}", secondsOfDay, time, mask);
                    }
                    mask <<= 1;
                }
            }
        }
        return times;
    }
}
