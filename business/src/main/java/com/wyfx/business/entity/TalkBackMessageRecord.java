package com.wyfx.business.entity;

import java.util.Date;

public class TalkBackMessageRecord {
    private Long id;

    private Long groupId;

    private Long fromUser;

    private Date sendTime;

    private Integer readStatus;

    private byte[] message;

    public TalkBackMessageRecord(Long id, Long groupId, Long fromUser, Date sendTime, Integer readStatus) {
        this.id = id;
        this.groupId = groupId;
        this.fromUser = fromUser;
        this.sendTime = sendTime;
        this.readStatus = readStatus;
    }

    public TalkBackMessageRecord(Long id, Long groupId, Long fromUser, Date sendTime, Integer readStatus, byte[] message) {
        this.id = id;
        this.groupId = groupId;
        this.fromUser = fromUser;
        this.sendTime = sendTime;
        this.readStatus = readStatus;
        this.message = message;
    }

    public TalkBackMessageRecord() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getFromUser() {
        return fromUser;
    }

    public void setFromUser(Long fromUser) {
        this.fromUser = fromUser;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }
}