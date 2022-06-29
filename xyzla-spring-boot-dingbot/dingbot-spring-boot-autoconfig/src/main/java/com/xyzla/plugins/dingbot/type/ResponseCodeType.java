package com.xyzla.plugins.dingbot.type;

/**
 * 自定义接口返回类型
 */
public enum ResponseCodeType {

    /**
     * 消息发送成功
     */
    OK(0),
    /**
     * 自定义相应码，非钉钉返回码值
     */
    UNKNOWN(9999);

    ResponseCodeType(Integer value) {
        this.value = value;
    }

    private Integer value;

    public Integer getValue() {
        return value;
    }
}
