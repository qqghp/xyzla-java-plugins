package com.xyzla.common.exception;

import java.io.Serializable;

public class SignatureRuntimException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 4628932112323013092L;

    public SignatureRuntimException() {
    }

    public SignatureRuntimException(String message) {
        super(message);
    }

    public SignatureRuntimException(String message, Throwable e) {
        super(message, e);
    }

    public SignatureRuntimException(Throwable cause) {
        super(cause);
    }
}
