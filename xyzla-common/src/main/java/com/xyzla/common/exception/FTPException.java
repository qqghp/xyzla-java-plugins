package com.xyzla.common.exception;

public class FTPException extends RuntimeException {

    public FTPException(String message) {
        super(message);
    }

    public FTPException(String message, Exception exception) {
        super(message, exception);
    }

}
