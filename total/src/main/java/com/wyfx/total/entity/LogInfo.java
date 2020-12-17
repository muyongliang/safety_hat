package com.wyfx.total.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

public class LogInfo {

    private Integer lid;

    @Excel(name = "操作者", orderNum = "0")
    private String executor;

    /**
     * 值的替换  导出是{a_id,b_id} 导入反过来,所以只用写一个
     */
    @Excel(name = "操作类型", orderNum = "1", replace = {"增加_0", "删除_1", "更新_2"})//操作类型{0:增加;1:删除;2:更新}
    private Integer taskType;

    @Excel(name = "操作行为", orderNum = "2")
    private String executDescribe;

    @Excel(name = "相关数据", orderNum = "3")
    private String relatedData;

    @Excel(name = "时间", orderNum = "4", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date ctime;

    public LogInfo(Integer lid, String executor, Integer taskType, String executDescribe, Date ctime, String relatedData) {
        this.lid = lid;
        this.executor = executor;
        this.taskType = taskType;
        this.executDescribe = executDescribe;
        this.ctime = ctime;
        this.relatedData = relatedData;
    }

    public LogInfo() {
        super();
    }

    public Integer getLid() {
        return lid;
    }

    public void setLid(Integer lid) {
        this.lid = lid;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor == null ? null : executor.trim();
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public String getExecutDescribe() {
        return executDescribe;
    }

    public void setExecutDescribe(String executDescribe) {
        this.executDescribe = executDescribe == null ? null : executDescribe.trim();
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getRelatedData() {
        return relatedData;
    }

    public void setRelatedData(String relatedData) {
        this.relatedData = relatedData == null ? null : relatedData.trim();
    }
}