package com.xyzla.common.exception;

public class RoadException extends XyzlaException {

    private static final long serialVersionUID = 2770315231512637987L;
    private String stepDesc;

    public RoadException() {
    }

    public RoadException(String stepDesc, Throwable e) {
        super(stepDesc, e);

        this.stepDesc = stepDesc;
    }

    public RoadException(String stepDesc) {
        super(stepDesc);

        this.stepDesc = stepDesc;
    }

    public String getStepDesc() {
        return stepDesc;
    }

    public void setStepDesc(String stepDesc) {
        this.stepDesc = stepDesc;
    }
}
