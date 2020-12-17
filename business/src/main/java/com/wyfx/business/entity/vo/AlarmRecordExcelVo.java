package com.wyfx.business.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

@Deprecated
public class AlarmRecordExcelVo {

    private Long ID;

    @Excel(name = "项目名称", orderNum = "1")
    private String projectName;

    @Excel(name = "报警类型", orderNum = "1")
    private String type;

    @Excel(name = "发送人姓名", orderNum = "1")
    private String name;

    @Excel(name = "账号", orderNum = "1")
    private String account;

    @Excel(name = "内容", orderNum = "1")
    private String message;

    @Excel(name = "时间", orderNum = "1")
    private String recordTime;

    public AlarmRecordExcelVo(Long ID, String projectName, String type, String name, String account, String message, String recordTime) {
        this.ID = ID;
        this.projectName = projectName;
        this.type = type;
        this.name = name;
        this.account = account;
        this.message = message;
        this.recordTime = recordTime;
    }

    public AlarmRecordExcelVo() {
    }

    @Override
    public String toString() {
        return "AlarmRecordExcelVo{" +
                "ID=" + ID +
                ", projectName='" + projectName + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", message='" + message + '\'' +
                ", recordTime=" + recordTime +
                '}';
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

}
