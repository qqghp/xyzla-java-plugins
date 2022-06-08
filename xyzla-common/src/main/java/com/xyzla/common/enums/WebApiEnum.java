package com.xyzla.common.enums;

public enum WebApiEnum {
    OK(0, "success"),

    REQUEST_PARAM_ERROR(1000, "requested paramaters are invalid"),
    INVALID_PASSWORD(1001, "invalid password"),
    INVALID_MOBILE_NUMBER(1002, "invalid phone number"),
    AUTHORIZATION_FAILED(1003, "authorization failed"),
    INTERNAL_ERROR(1004, "internal error"),
    USERID_NON_ASSOCIATE_EDITOR(
            1005, "please confirm the entered user ID is associated with your CMS account"),
    USERID_ASSOCIATE_MORE_EDITOR(
            1006, "please confirm user ID is only associated with one CMS account");

    private Integer code;
    private String message;

    WebApiEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
