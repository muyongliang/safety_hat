package com.wyfx.total.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * update by wsm on 2019-12-21
 * 导出excel使用
 */
public class Log {

    private Long logId;

    @Excel(name = "账号类型", orderNum = "0", replace = {"调度员_1", "企业管理员_0", "终端账号_2", "其他管理员账号_3"})
    private Integer accountType; //账号类型 账号类型{1:调度员,0:企业管理员;2:终端账号;3:其他管理员账号}

    @Excel(name = "操作者", orderNum = "1")
    private String executor;//操作者

    @Excel(name = "操作类型", orderNum = "2", replace = {"增加_1", "删除_2", "更新_3"})
    private Integer actionType;  //操作类型 增加 1 删除2 更新3
    @Excel(name = "操作行为", orderNum = "3")
    private String action;//操作行为
    @Excel(name = "相关数据", orderNum = "4")
    private String detail;//相关数据

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "时间", orderNum = "5", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    private Long businessId;

    private Long bid;//business_user_id

    public Log(Long logId, Integer accountType, String executor, Integer actionType, String action, String detail, Date time, Long businessId, Long bid) {
        this.logId = logId;
        this.accountType = accountType;
        this.executor = executor;
        this.actionType = actionType;
        this.action = action;
        this.detail = detail;
        this.time = time;
        this.businessId = businessId;
        this.bid = bid;
    }

    public Log() {
        super();
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor == null ? null : executor.trim();
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action == null ? null : action.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    @Override
    public String toString() {
        return "Log{" +
                "logId=" + logId +
                ", accountType=" + accountType +
                ", executor='" + executor + '\'' +
                ", actionType=" + actionType +
                ", action='" + action + '\'' +
                ", detail='" + detail + '\'' +
                ", time=" + time +
                ", businessId=" + businessId +
                ", bid=" + bid +
                '}';
    }
}