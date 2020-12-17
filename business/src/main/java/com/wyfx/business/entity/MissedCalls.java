package com.wyfx.business.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

/**
 * update by wsm on 2019-12-17
 */
public class MissedCalls {

    private Long missId;

    @Excel(name = "呼叫人", orderNum = "1")
    private String caller;//呼叫人

    @Excel(name = "时间", orderNum = "2", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date time;//时间

    @Excel(name = "性别", replace = {"全部_0", "语音通话_1", "语音会议_2", "视频通话_3", "视频会议_4"}, orderNum = "0")
    private Integer type;//通话类型{0:全部;1:语音通话;2:语音会议;3:视频通话;4:视频会议}

    private String reason;

    private Long bid;

    private Long answerId;

    private Boolean isView;

    private Long businessId;//企业ID

    public MissedCalls(Long missId, String caller, Date time, Integer type, String reason, Long bid, Long answerId, Boolean isView, Long businessId) {
        this.missId = missId;
        this.caller = caller;
        this.time = time;
        this.type = type;
        this.reason = reason;
        this.bid = bid;
        this.answerId = answerId;
        this.isView = isView;
        this.businessId = businessId;
    }

    public MissedCalls() {
        super();
    }

    public Long getMissId() {
        return missId;
    }

    public void setMissId(Long missId) {
        this.missId = missId;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller == null ? null : caller.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Boolean getIsView() {
        return isView;
    }

    public void setIsView(Boolean view) {
        isView = view;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }
}