package com.xyzla.mq.kafka;

public class ConsumerMessageBO<T> {

    private long offset;
    private int partition;

    private T t;

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "ConsumerMessageBO{" +
                "offset=" + offset +
                ", partition=" + partition +
                ", entity='" + t.toString() + '\'' +
                '}';
    }
}
