package com.wyfx.business.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class DeviceInfo {
    private Long did;

    private String number;

    private Integer type;

    private String imei;

    private Integer status;

    private String recordPerson;

    private Date recordTime;

    private String updatePerson;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Integer source;

    private Integer onlineFlag;

    private Long businessId;

    public DeviceInfo(Long did, String number, Integer type, String imei, Integer status, String recordPerson, Date recordTime, String updatePerson, Date updateTime, Integer source, Integer onlineFlag, Long businessId) {
        this.did = did;
        this.number = number;
        this.type = type;
        this.imei = imei;
        this.status = status;
        this.recordPerson = recordPerson;
        this.recordTime = recordTime;
        this.updatePerson = updatePerson;
        this.updateTime = updateTime;
        this.source = source;
        this.onlineFlag = onlineFlag;
        this.businessId = businessId;
    }

    public DeviceInfo() {
        super();
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number == null ? null : number.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei == null ? null : imei.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRecordPerson() {
        return recordPerson;
    }

    public void setRecordPerson(String recordPerson) {
        this.recordPerson = recordPerson == null ? null : recordPerson.trim();
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson == null ? null : updatePerson.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getOnlineFlag() {
        return onlineFlag;
    }

    public void setOnlineFlag(Integer onlineFlag) {
        this.onlineFlag = onlineFlag;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "did=" + did +
                ", number='" + number + '\'' +
                ", type=" + type +
                ", imei='" + imei + '\'' +
                ", status=" + status +
                ", recordPerson='" + recordPerson + '\'' +
                ", recordTime=" + recordTime +
                ", updatePerson='" + updatePerson + '\'' +
                ", updateTime=" + updateTime +
                ", source=" + source +
                ", onlineFlag=" + onlineFlag +
                ", businessId=" + businessId +
                '}';
    }
}