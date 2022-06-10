package com.xyzla.common.exception;

public class ApiAccessException extends Exception {
    private Integer code;
    private String msg;

    private ApiAccessException() {
    }

    public ApiAccessException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
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
}
