package com.xyzla.dingbot.monitor;

import com.xyzla.dingbot.monitor.cache.Cache;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.xyzla.plugins.dingbot.client.DingbotClient;
import com.xyzla.plugins.dingbot.entity.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FlowMonitor {

    private static Logger logger = LoggerFactory.getLogger(FlowMonitor.class);

    // 日用量 阀值，单位 KB, 默认: 1 * 1024 * 1024L
    @Value("${monitor.flow-usage.daily-threshold:1048576}")
    private Long DAILY_FLOW_USAGE_THRESHOLD;
    // 月用量 阀值，单位 KB, 默认: 30 * 1024 * 1024L
    @Value("${monitor.flow-usage.monthly-threshold:31457280}")
    private Long MONTHLY_FLOW_USAGE_THRESHOLD;

    // 日用量 超过阀值 告警 队列
    private static final String DAILY_FLOW_USAGE_THRESHOLD_ALARM = "DAILY_FLOW_USAGE_THRESHOLD_ALARM";
    // 月用量 超过阀值 告警 队列
    private static final String MONTHLY_FLOW_USAGE_THRESHOLD_ALARM = "MONTHLY_FLOW_USAGE_THRESHOLD_ALARM";

    private static Cache<String, List<AlarmEntity>> cache = new Cache<String, List<AlarmEntity>>();

    // 过期时间 单位 分钟
    // 需与  @Scheduled cron 保持一致
    private static final int EXPIRE_TIME = 10;

    @Autowired
    @Qualifier("dingbotClient")
    private DingbotClient client;

    // 日用量 报警, 单位 KB, true 超过阀值
    public boolean dailyAlarm(String iccid, Long dailyUsage) {
        return dailyAlarm(iccid, dailyUsage, DAILY_FLOW_USAGE_THRESHOLD);
    }

    // 日用量 报警, 单位 KB, true 超过阀值
    public boolean dailyAlarm(String iccid, Long dailyUsage, Long dailyUsageThreshold) {
        if (dailyUsage != null && dailyUsage.compareTo(dailyUsageThreshold) >= 0) {
            logger.warn("daily usage alarm, iccid: {}, {} > {}", iccid, dailyUsage, dailyUsageThreshold);
            List<AlarmEntity> list = cache.get(DAILY_FLOW_USAGE_THRESHOLD_ALARM);
            if (list == null) {
                list = Collections.synchronizedList(new ArrayList<>());
            }
            list.add(new AlarmEntity(iccid, dailyUsage, dailyUsageThreshold));

            cache.put(DAILY_FLOW_USAGE_THRESHOLD_ALARM, list, getExpireSecond(), TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    // 月用量 报警, 单位 KB, true 超过阀值
    public boolean monthlyAlarm(String iccid, Long monthlyUsage) {
        return monthlyAlarm(iccid, monthlyUsage, MONTHLY_FLOW_USAGE_THRESHOLD);
    }

    // 月用量 报警, 单位 KB, true 超过阀值
    public boolean monthlyAlarm(String iccid, Long monthlyUsage, Long monthlyUsageThreshold) {
        if (monthlyUsage != null && monthlyUsage.compareTo(monthlyUsageThreshold) >= 0) {
            logger.warn("monthly usage alarm, iccid: {}, {} > {}", iccid, monthlyUsage, monthlyUsageThreshold);
            List<AlarmEntity> list = cache.get(MONTHLY_FLOW_USAGE_THRESHOLD_ALARM);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(new AlarmEntity(iccid, monthlyUsage, monthlyUsageThreshold));
            cache.put(MONTHLY_FLOW_USAGE_THRESHOLD_ALARM, list, getExpireSecond(), TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    public int getExpireSecond() {
        return getExpireSecond(LocalDateTime.now());
    }

    public int getExpireSecond(LocalDateTime localDateTime) {
        Duration durationMillis = Duration.between(localDateTime, localDateTime.plusMinutes(1L).withSecond(0).withNano(0));
        //
        int n = (int) Math.floor(localDateTime.getMinute() / EXPIRE_TIME);
        //
        int m = (int) Math.floor(localDateTime.getMinute() % EXPIRE_TIME);

        Duration durationMinute = Duration.between(localDateTime, localDateTime.plusMinutes((m == (EXPIRE_TIME - 1)) ? 1 : (EXPIRE_TIME - m)).withSecond(0).withNano(0));
        if (logger.isDebugEnabled()) {
            if (logger.isDebugEnabled()) {
                logger.debug(
                        "next minute: {},current minute: {},duration seconds: {},n: {},m: {},current minute remaining seconds: {}",
                        localDateTime.plusMinutes((m == 9) ? 1 : (EXPIRE_TIME - m)).withSecond(0).withNano(0),
                        localDateTime,
                        (int) (durationMinute.toMillis() / 1000),
                        n,
                        m,
                        (int) (durationMillis.toMillis() / 1000));
            }
        }

        return (m == (EXPIRE_TIME - 1) ? 0 : (EXPIRE_TIME - m - 1)) * 60 + (int) (durationMillis.toMillis() / 1000) + 2;
    }

    // @Scheduled(cron = "00/10 * * * * *")
    @Scheduled(cron = "00 00/10 * * * *")
    public void scheduledTask() {
        try {
            StringBuilder msg = new StringBuilder();
            // 日用量 报警信息发送
            List<AlarmEntity> dailyFlowUsages = cache.get(DAILY_FLOW_USAGE_THRESHOLD_ALARM);
            if (dailyFlowUsages != null && dailyFlowUsages.size() > 0) {
                msg.append("Daily Flow Usage Alarm. ").append("Total: ").append(dailyFlowUsages.size()).append(" ICCID Cards\n");
                if (dailyFlowUsages.size() < 7) {
                    for (AlarmEntity alarmEntity : dailyFlowUsages) {
                        msg.append("> ").append(alarmEntity.getIccid()).append(" - ").append((int) (alarmEntity.getUsage() / 1024)).append(" - ").append((int) (alarmEntity.getUsageThreshold() / 1024)).append("\n");
                    }
                }
                msg.append("In the last ").append(EXPIRE_TIME).append(" minutes. Please retrieve <Irene> log keywords: Daily Flow Usage Alarm\n\n");
                dailyFlowUsages.clear();
            }
            //月用量 报警信息发送
            List<AlarmEntity> monthlyFlowUsages = cache.get(MONTHLY_FLOW_USAGE_THRESHOLD_ALARM);
            if (monthlyFlowUsages != null && monthlyFlowUsages.size() > 0) {
                msg.append("Monthly Flow Usage Alarm. ").append("Total: ").append(monthlyFlowUsages.size()).append(" ICCID Cards\n");
                if (monthlyFlowUsages.size() < 7) {
                    for (AlarmEntity alarmEntity : monthlyFlowUsages) {
                        msg.append("> ").append(alarmEntity.getIccid()).append(" - ").append((int) (alarmEntity.getUsage() / 1024)).append(" - ").append((int) (alarmEntity.getUsageThreshold() / 1024)).append("\n");
                    }
                }
                msg.append("In the last ").append(EXPIRE_TIME).append(" minutes. Please retrieve <Irene> log keywords: Monthly Flow Usage Alarm\n\n");
                monthlyFlowUsages.clear();
            }

            if (msg.toString().length() > 10) {
                msg.append("e.g.: ICCID - Flow Usage / MB - Flow Usage Threshold / MB\n");
                msg.append("at time ").append(LocalDateTime.now()).append(", sended by ").append(getHostName());
                client.sendMessage(new TextMessage(msg.toString()));
            }
        } catch (Exception ex) {
            logger.error("flow usage monitor error", ex);
        }
        logger.debug("flow usage monitor done, scheduled time：{}, expire time: {} minute", LocalDateTime.now(), EXPIRE_TIME);
    }

    private String getHostName() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "Unknow Hostname";
    }
}
