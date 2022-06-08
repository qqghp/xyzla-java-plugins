package com.xyzla.common.exception;

public class XyzlaException extends Exception {

    private static final long serialVersionUID = 8578222283400246622L;

    public XyzlaException() {
    }

    public XyzlaException(String message) {
        super(message);
    }

    public XyzlaException(String message, Throwable e) {
        super(message, e);
    }
}
