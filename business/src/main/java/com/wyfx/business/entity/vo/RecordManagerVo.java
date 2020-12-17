package com.wyfx.business.entity.vo;


public class RecordManagerVo {

    private Long id;

    private String recordTime;

    private Long pid;

    private String projectName;
    private Long sendBid;
    private String sendName;
    private Long sendToBid;
    private String sendToName;
    private String message;
    private Integer messageType;
    private Long groupId;
    private Integer type;
    private String startTime;
    private String endTime;
    private String account;
    private String title;
    private Long businessId;
    private String callerAccount;//呼叫人账号
    private String calledAccount;//被叫人账号
    private String recordBy;//录音人账号
    private Integer userType;//账号类型

    public RecordManagerVo() {

    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public String getRecordBy() {
        return recordBy;
    }

    public void setRecordBy(String recordBy) {
        this.recordBy = recordBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = ("".equals(title)) ? null : title;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = ("".equals(account)) ? null : account;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = ("".equals(startTime)) ? null : startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = ("".equals(endTime)) ? null : endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getSendBid() {
        return sendBid;
    }

    public void setSendBid(Long sendBid) {
        this.sendBid = sendBid;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = ("".equals(sendName)) ? null : sendName;
    }

    public Long getSendToBid() {
        return sendToBid;
    }

    public void setSendToBid(Long sendToBid) {
        this.sendToBid = sendToBid;
    }

    public String getSendToName() {
        return sendToName;
    }

    public void setSendToName(String sendToName) {
        this.sendToName = ("".equals(sendToName)) ? null : sendToName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
