package com.wyfx.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class RecordManager {

    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date recordTime;

    private Long pid;

    private Long sendBid;

    private String sendName;

    private String sendToBid;

    private String sendToName;

    private String message;

    private Integer messageType;

    private Long groupId;

    private Integer type;

    private Long parentId;

    private String title;
    private String recordBy;//录音人账号


    public RecordManager() {
    }

    public RecordManager(Long id, Date recordTime, Long pid, Long sendBid, String sendName, String sendToBid, String sendToName, String message, Integer messageType, Long groupId, Integer type, Long parentId, String title, String recordBy) {
        this.id = id;
        this.recordTime = recordTime;
        this.pid = pid;
        this.sendBid = sendBid;
        this.sendName = sendName;
        this.sendToBid = sendToBid;
        this.sendToName = sendToName;
        this.message = message;
        this.messageType = messageType;
        this.groupId = groupId;
        this.type = type;
        this.parentId = parentId;
        this.title = title;
        this.recordBy = recordBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getSendBid() {
        return sendBid;
    }

    public void setSendBid(Long sendBid) {
        this.sendBid = sendBid;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getSendToBid() {
        return sendToBid;
    }

    public void setSendToBid(String sendToBid) {
        this.sendToBid = sendToBid;
    }

    public String getSendToName() {
        return sendToName;
    }

    public void setSendToName(String sendToName) {
        this.sendToName = sendToName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getRecordBy() {
        return recordBy;
    }

    public void setRecordBy(String recordBy) {
        this.recordBy = recordBy;
    }
}
