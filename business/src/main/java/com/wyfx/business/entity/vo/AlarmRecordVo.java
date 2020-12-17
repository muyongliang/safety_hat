package com.wyfx.business.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

/**
 * 2020-3-11 添加excel导入样式字段
 */
public class AlarmRecordVo {

    private Long id;

    @Excel(name = "时间", orderNum = "1", format = "yyyy-MM-dd HH:mm:ss")
    private Date recordTime;

    @Excel(name = "报警类型", orderNum = "1", replace = {"SOS报警_1", " 围栏报警_2", " 跌落报警_3", " 脱离报警_4", "高空报警_5"})
    private Long aid;

    private Boolean isView;

    private Long bid;

    private Long pid;

    @Excel(name = "项目名称", orderNum = "1")
    private String projectName;

    private String message;

    @Excel(name = "发送人姓名", orderNum = "1")
    private String name;

    @Excel(name = "账号", orderNum = "1")
    private String account;

    @Excel(name = "内容", orderNum = "1")
    private String content;

    private String bids;

    private String startTime;

    private String endTime;

    private String sendName;

    private String workType;

    private String color;

    private Integer onlineStatus;

    private Double latitude;//纬度

    private Double longitude;//经度

    private Long businessId;//企业Id

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getBids() {
        return bids;
    }

    public void setBids(String bids) {
        this.bids = bids;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
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

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }
}
