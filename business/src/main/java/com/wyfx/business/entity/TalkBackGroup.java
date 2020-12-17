package com.wyfx.business.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TalkBackGroup {

    public static final int NOT_ACTIVATE = 0;//未激活状态

    public static final int ACTIVATE = 1;//激活状态

    private Long id;

    private String name;

    private Integer isActivate;

    private Integer isRecording;

    private Integer isLock;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String createPerson;

    private Long bid;

    private Long projectId;

    public TalkBackGroup(Long id, String name, Integer isActivate, Integer isRecording, Integer isLock, Date createTime, String createPerson, Long bid, Long projectId) {
        this.id = id;
        this.name = name;
        this.isActivate = isActivate;
        this.isRecording = isRecording;
        this.isLock = isLock;
        this.createTime = createTime;
        this.createPerson = createPerson;
        this.bid = bid;
        this.projectId = projectId;
    }

    public TalkBackGroup(String name, Integer isActivate, Integer isRecording, Integer isLock, Date createTime, String createPerson, Long bid) {
        this.name = name;
        this.isActivate = isActivate;
        this.isRecording = isRecording;
        this.isLock = isLock;
        this.createTime = createTime;
        this.createPerson = createPerson;
        this.bid = bid;
    }

    public TalkBackGroup() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getIsActivate() {
        return isActivate;
    }

    public void setIsActivate(Integer isActivate) {
        this.isActivate = isActivate;
    }

    public Integer getIsRecording() {
        return isRecording;
    }

    public void setIsRecording(Integer isRecording) {
        this.isRecording = isRecording;
    }

    public Integer getIsLock() {
        return isLock;
    }

    public void setIsLock(Integer isLock) {
        this.isLock = isLock;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}