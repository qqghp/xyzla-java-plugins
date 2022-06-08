package com.xyzla.common.exception;

public class CarrierApiException extends Exception {

    /**
     * Constructor
     *
     * @param message 异常消息
     */
    public CarrierApiException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param exp {@link Throwable throwable}
     */
    public CarrierApiException(Throwable exp) {
        super(exp);
    }
}