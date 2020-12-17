package com.wyfx.business.entity.vo;

import java.util.Date;

public class TalkBackRecordVo {

    private Long id;

    private Long pid;

    private Long groupId;

    private String projectName;

    private String groupName;

    private String message;

    private Integer countNum;

    private Date recordTime;

    private Long businessId;

    public TalkBackRecordVo(Long id, Long pid, Long groupId, String projectName, String groupName, String message, Integer countNum, Date recordTime) {
        this.id = id;
        this.pid = pid;
        this.groupId = groupId;
        this.projectName = projectName;
        this.groupName = groupName;
        this.message = message;
        this.countNum = countNum;
        this.recordTime = recordTime;
    }

    public TalkBackRecordVo() {
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getCountNum() {
        return countNum;
    }

    public void setCountNum(Integer countNum) {
        this.countNum = countNum;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
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

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }
}
