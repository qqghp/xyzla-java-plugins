package com.xyzla.spring.exception;


public class MongoException extends RuntimeException {

    private String message;

    /**
     * Constructor
     *
     * @param message 异常消息
     */
    public MongoException(String message) {
        super(message);
        this.message = message;
    }
}
