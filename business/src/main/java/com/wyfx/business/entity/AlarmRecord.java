package com.wyfx.business.entity;

import java.util.Date;

public class AlarmRecord {

    private Long id;

    private Date recordTime;

    private Long aid;

    private Boolean isView;

    private Long bid;

    private String message;

    private Long pid;

    private String sendName;

    private Double latitude;//纬度

    private Double longitude;//经度

    public AlarmRecord(Long id, Date recordTime, Long aid, Boolean isView, Long bid) {
        this.id = id;
        this.recordTime = recordTime;
        this.aid = aid;
        this.isView = isView;
        this.bid = bid;
    }

    public AlarmRecord(Long id, Date recordTime, Long aid, Boolean isView, Long bid, String message, Long pid, String sendName) {
        this.id = id;
        this.recordTime = recordTime;
        this.aid = aid;
        this.isView = isView;
        this.bid = bid;
        this.message = message;
        this.pid = pid;
        this.sendName = sendName;
    }

    public AlarmRecord(Long id, Date recordTime, Long aid, Boolean isView, Long bid, String message, Long pid, String sendName, Double latitude, Double longitude) {
        this.id = id;
        this.recordTime = recordTime;
        this.aid = aid;
        this.isView = isView;
        this.bid = bid;
        this.message = message;
        this.pid = pid;
        this.sendName = sendName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public AlarmRecord() {

    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public Boolean getView() {
        return isView;
    }

    public void setView(Boolean view) {
        isView = view;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
