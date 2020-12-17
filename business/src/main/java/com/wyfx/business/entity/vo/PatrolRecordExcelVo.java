package com.wyfx.business.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 巡检记录导出模版
 */

public class PatrolRecordExcelVo {

    @Excel(name = "项目名称", orderNum = "1")
    private String projectName;

    @Excel(name = "发送人姓名", orderNum = "1")
    private String sendName;

    @Excel(name = "账号", orderNum = "1")
    private String account;

    @Excel(name = "标题", orderNum = "1")
    private String title;

    @Excel(name = "提交时间", orderNum = "1")
    private String recordTime;

    public PatrolRecordExcelVo(String projectName, String sendName, String account, String title, String recordTime) {
        this.projectName = projectName;
        this.sendName = sendName;
        this.account = account;
        this.title = title;
        this.recordTime = recordTime;
    }

    public PatrolRecordExcelVo() {
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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
