package com.xyzla.mq.exception;


public class ConsumerUnconfiguredException extends RuntimeException {

    public ConsumerUnconfiguredException(String message) {
    }

    public ConsumerUnconfiguredException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
