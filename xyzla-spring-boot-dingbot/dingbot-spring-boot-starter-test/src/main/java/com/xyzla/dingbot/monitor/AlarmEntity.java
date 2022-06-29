package com.xyzla.dingbot.monitor;

public class AlarmEntity {
    private String iccid;
    private Long usage;
    private Long usageThreshold;

    public AlarmEntity() {}

    public AlarmEntity(String iccid, Long usage, Long usageThreshold) {
        this.iccid = iccid;
        this.usage = usage;
        this.usageThreshold = usageThreshold;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public Long getUsage() {
        return usage;
    }

    public void setUsage(Long usage) {
        this.usage = usage;
    }

    public Long getUsageThreshold() {
        return usageThreshold;
    }

    public void setUsageThreshold(Long usageThreshold) {
        this.usageThreshold = usageThreshold;
    }

    @Override
    public String toString() {
        return "AlarmEntity{"
                + "iccid='"
                + iccid
                + '\''
                + ", usage="
                + usage
                + ", usageThreshold="
                + usageThreshold
                + '}';
    }
}
