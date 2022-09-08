package com.xyzla.redis.entity;

public class Device {

    private Integer id;

    private String iccid;
    private String imei;
    private String mac;

    public Device() {

    }

    public Device(Integer id, String iccid, String imei, String mac) {
        this.id = id;
        this.iccid = iccid;
        this.imei = imei;
        this.mac = mac;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
