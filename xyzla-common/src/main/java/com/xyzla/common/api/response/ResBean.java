package com.xyzla.common.api.response;


import com.xyzla.common.enums.WebApiEnum;

public class ResBean {

    private Integer code;
    private String msg;
    private Object data;

    public ResBean(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResBean(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ResBean of(Integer code, String msg, Object data) {
        return new ResBean(code, msg, data);
    }

    public static ResBean ofStatus(WebApiEnum status, Object data) {
        return of(status.getCode(), status.getMessage(), data);
    }

    public static ResBean ofStatus(WebApiEnum status) {
        return ofStatus(status, null);
    }

    public static ResBean ofSuccess(Object data) {
        return ofStatus(WebApiEnum.OK, data);
    }

    public static ResBean ofSuccess() {
        return ofStatus(WebApiEnum.OK, null);
    }

    public static ResBean ofServerError(String msg) {
        return of(WebApiEnum.INTERNAL_ERROR.getCode(), msg, null);
    }

    public static ResBean ofServerError(Integer code, String msg) {
        return of(code, msg, null);
    }
}
