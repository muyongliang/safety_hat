package com.wyfx.business.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;


public class TalkBackRecordExcelVo {

    @Excel(name = "账号类型", orderNum = "1", replace = {"调度员_1", "终端_2"})
    private String userType;

    @Excel(name = "发送人姓名", orderNum = "1")
    private String sendName;

    @Excel(name = "账号", orderNum = "1")
    private String account;

    @Excel(name = "时间", orderNum = "1")
    private String recordTime;

    public TalkBackRecordExcelVo(String userType, String sendName, String account, String recordTime) {
        this.userType = userType;
        this.sendName = sendName;
        this.account = account;
        this.recordTime = recordTime;
    }

    public TalkBackRecordExcelVo() {
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
