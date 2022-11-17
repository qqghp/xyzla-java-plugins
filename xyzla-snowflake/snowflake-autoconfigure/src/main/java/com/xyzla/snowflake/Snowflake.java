package com.xyzla.snowflake;

import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Snowflake {
    /**
     * 开始时间截 ( 2022-10-01 ) 服务一旦运行过之后不能修改。会导致 ID 生成重复
     */
    private final long twepoch = 1664553600000L;

    /**
     * 机器 ID 所占的位数 0 - 64
     */
    private final long workerIdBits = 6L;

    /**
     * 工作组 ID 所占的位数 0 - 16
     */
    private final long dataCenterIdBits = 4L;

    /**
     * 支持的最大机器 id，结果是 63 ( 这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数 )
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 支持的最大数据标识 id，结果是 15
     */
    private final long maxDatacenterId = -1L ^ (-1L << dataCenterIdBits);

    /**
     * 序列在 id 中占的位数
     */
    private final long sequenceBits = 12L;

    /**
     * 机器 ID 向左移 12 位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据标识 ID 向左移 18 位 ( 12 + 6 )
     */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间截向左移 22 位 ( 12 + 6 + 4 )
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

    /**
     * 生成序列的掩码，这里为 4095 ( 0b111111111111 = 0xfff = 4095 )
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 工作机器 ID ( 0 ~ 63 )
     */
    private static long workerId;

    /**
     * 数据中心 ID ( 0 ~ 16 )
     */
    private long datacenterId;

    /**
     * 毫秒内序列 ( 0 ~ 4095 )
     */
    private long sequence = 0L;

    /**
     * 上次生成 ID 的时间截
     */
    private long lastTimestamp = -1L;

    // ==============================Constructors=====================================

    /**
     * 构造函数
     *
     * @param workerId     工作 ID ( 0 ~ 63)
     * @param datacenterId 数据中心 ID ( 0 ~ 15 )
     */
    public Snowflake(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(
                    String.format(
                            "worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(
                    String.format(
                            "datacenter Id can't be greater than %d or less than 0",
                            maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 构造函数
     */
    public Snowflake() {
        this.workerId = 0;
        this.datacenterId = 0;
    }

    /**
     * 获得下一个 ID ( 该方法是线程安全的 )
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format(
                            "Clock moved backwards. Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp));
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 毫秒内序列溢出
            if (sequence == 0) {
                // 阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        // 时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        // 上次生成ID的时间截
        lastTimestamp = timestamp;

        // 移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift) //
                | (datacenterId << datacenterIdShift) //
                | (workerId << workerIdShift) //
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成 ID 的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间 ( 毫秒 )
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public long getWorkerId() {
        return workerId;
    }

    public static void setWorkerId(long workerId) {
        Snowflake.workerId = workerId;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(long datacenterId) {
        this.datacenterId = datacenterId;
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(-1));
        System.out.println(Integer.toBinaryString(-3));
        System.out.println(Integer.toBinaryString(1));
        System.out.println(Integer.toBinaryString(3));

        Snowflake idWorker = new Snowflake(0, 0);
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < 4000; i++) {
            long id = idWorker.nextId();
            list.add(id);
        }
        list.stream().forEach((n) -> System.out.println(Long.toBinaryString(n) + "    " + n));
    }
}
