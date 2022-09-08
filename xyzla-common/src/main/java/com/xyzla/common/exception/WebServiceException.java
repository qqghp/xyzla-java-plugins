package com.xyzla.common.exception;

@Deprecated
public class WebServiceException extends RuntimeException {

    public WebServiceException() {
        super();
    }

    public WebServiceException(String message) {
        super(message);
    }

    public WebServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebServiceException(Throwable cause) {
        super(cause);
    }
}
