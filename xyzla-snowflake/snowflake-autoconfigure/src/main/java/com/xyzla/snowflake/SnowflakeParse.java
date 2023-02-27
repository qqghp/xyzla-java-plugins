package com.xyzla.snowflake;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * https://blog.csdn.net/weixin_30735391/article/details/94849504
 */
public class SnowflakeParse {

    private static long twepoch = 1664553600000L;

    private long workerIdBits = 6L;
    private long datacenterIdBits = 4L;

    private static final long sequenceBits = 12L;

    private long workerIdShift = sequenceBits;
    private long dataCenterIdShift = sequenceBits + workerIdBits;
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    static {
    }

    public void parseInfo(long id) {
        String sonwFlakeId = Long.toBinaryString(id);
        int len = sonwFlakeId.length();

        int sequenceStart = (int) (len < workerIdShift ? 0 : len - workerIdShift);
        int workerStart = (int) (len < dataCenterIdShift ? 0 : len - dataCenterIdShift);
        int timeStart = (int) (len < timestampLeftShift ? 0 : len - timestampLeftShift);
        String sequence = sonwFlakeId.substring(sequenceStart, len);
        String workerId = sequenceStart == 0 ? "0" : sonwFlakeId.substring(workerStart, sequenceStart);
        String dataCenterId = workerStart == 0 ? "0" : sonwFlakeId.substring(timeStart, workerStart);
        String time = timeStart == 0 ? "0" : sonwFlakeId.substring(0, timeStart);
        int sequenceInt = Integer.valueOf(sequence, 2);
        System.out.println("sequence " + sequenceInt);
        int workerIdInt = Integer.valueOf(workerId, 2);
        System.out.println("workerId " + workerIdInt);
        int dataCenterIdInt = Integer.valueOf(dataCenterId, 2);
        System.out.println("datacenter " + dataCenterIdInt);
        long diffTime = Long.parseLong(time, 2);
        long timeLong = diffTime + twepoch;
        LocalDateTime date = timestamp2LocalDateTime(timeLong);
        System.out.println("date " + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS")));
        System.out.println();
    }


    public static LocalDateTime timestamp2LocalDateTime(Long timestamp) {
//        long timestamp = System.currentTimeMillis();
//        LocalDate localDate = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDate();
        LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        return localDateTime;
    }

    public static void main(String[] args) {
        SnowflakeParse sonwFlakeId = new SnowflakeParse();
        sonwFlakeId.parseInfo(44120613454884864l);
        sonwFlakeId.parseInfo(44120791293370368l);
        sonwFlakeId.parseInfo(44120854560247808l);

    }

}
