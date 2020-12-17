package com.wyfx.business.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;


public class PhoneRecordExcelVo {

    @Excel(name = "呼叫人姓名", orderNum = "1")
    private String sendName;

    @Excel(name = "呼叫人账号", orderNum = "1")
    private String callerAccount;

    @Excel(name = "被叫人姓名", orderNum = "1")
    private String sendToName;

    @Excel(name = "被叫人账号", orderNum = "1")
    private String calledAccount;

    @Excel(name = "录音方", orderNum = "1")
    private String messageType;

    @Excel(name = "录音时长", orderNum = "1")
    private String title;

    @Excel(name = "录音时间", orderNum = "1")
    private String recordTime;

    public PhoneRecordExcelVo(String sendName, String callerAccount, String sendToName, String calledAccount, String messageType, String title, String recordTime) {
        this.sendName = sendName;
        this.callerAccount = callerAccount;
        this.sendToName = sendToName;
        this.calledAccount = calledAccount;
        this.messageType = messageType;
        this.title = title;
        this.recordTime = recordTime;
    }

    public PhoneRecordExcelVo() {
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getCallerAccount() {
        return callerAccount;
    }

    public void setCallerAccount(String callerAccount) {
        this.callerAccount = callerAccount;
    }

    public String getCalledAccount() {
        return calledAccount;
    }

    public void setCalledAccount(String calledAccount) {
        this.calledAccount = calledAccount;
    }

    public String getSendToName() {
        return sendToName;
    }

    public void setSendToName(String sendToName) {
        this.sendToName = sendToName;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        if ("1".equals(messageType)) {
            messageType = "被叫方";
        } else {
            messageType = "呼叫方";
        }
        this.messageType = messageType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }
}
