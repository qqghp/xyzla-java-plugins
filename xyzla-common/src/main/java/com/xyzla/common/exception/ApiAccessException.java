package com.xyzla.common.exception;

import com.xyzla.common.util.JacksonUtil;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;

import java.util.HashMap;
import java.util.Map;

public class ApiAccessException extends RuntimeException {
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

    @Override
    public String toString() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("code", code);
        paramMap.put("msg", msg);
        return JacksonUtil.toJson(paramMap);
    }
}
