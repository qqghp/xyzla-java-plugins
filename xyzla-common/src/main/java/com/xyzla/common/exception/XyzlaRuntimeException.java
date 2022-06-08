package com.xyzla.common.exception;

import java.io.Serializable;

public class XyzlaRuntimeException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 4261985541496363601L;

    public XyzlaRuntimeException() {
    }

    public XyzlaRuntimeException(String message) {
        super(message);
    }

    public XyzlaRuntimeException(String message, Throwable e) {
        super(message, e);
    }

    public XyzlaRuntimeException(Throwable cause) {
        super(cause);
    }
}
