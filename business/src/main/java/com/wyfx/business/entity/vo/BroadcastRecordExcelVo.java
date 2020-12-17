package com.wyfx.business.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 广播记录导出模版
 */

public class BroadcastRecordExcelVo {

    @Excel(name = "发送人姓名", orderNum = "1")
    private String sendName;

    @Excel(name = "账号", orderNum = "1")
    private String account;

    @Excel(name = "发送方式", orderNum = "1")
    private String messageType;

    @Excel(name = "时间", orderNum = "1")
    private String recordTime;

    public BroadcastRecordExcelVo() {
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        if ("1".equals(messageType)) {
            messageType = "文字";
        } else {
            messageType = "语音";
        }
        this.messageType = messageType;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

}
