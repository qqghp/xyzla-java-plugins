package com.xyzla.entity;

public class TopicDemoEntity {
    private String uuid;
    private String sequenceId;

    public TopicDemoEntity() {
    }

    public TopicDemoEntity(String uuid, String sequenceId) {
        this.uuid = uuid;
        this.sequenceId = sequenceId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public String toString() {
        return "TopicDemoEntity{" +
                "uuid='" + uuid + '\'' +
                ", sequenceId='" + sequenceId + '\'' +
                '}';
    }
}
