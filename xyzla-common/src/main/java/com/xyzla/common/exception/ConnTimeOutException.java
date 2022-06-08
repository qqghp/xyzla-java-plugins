package com.xyzla.common.exception;

public class ConnTimeOutException extends RoadException {

    private static final long serialVersionUID = 3866407612162579198L;

    public ConnTimeOutException() {
        // TODO Auto-generated constructor stub
    }

    public ConnTimeOutException(String stepDesc) {
        super(stepDesc);
    }

    public ConnTimeOutException(String stepDesc, Throwable e) {
        super(stepDesc, e);
    }
}
