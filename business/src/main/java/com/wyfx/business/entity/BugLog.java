package com.wyfx.business.entity;

/**
 * create by wsm on 2019-1-11
 * 崩溃日志记录
 */
public class BugLog {
    private Integer bugId; //日志id

    private String name; //账号

    private Integer type; //用户类型 1 调度员 2 终端

    private Integer businessId; //用户在business_user 中的id

    private String content; //崩溃日志内容 数据库保存text类型

    public BugLog(Integer bugId, String name, Integer type, Integer businessId) {
        this.bugId = bugId;
        this.name = name;
        this.type = type;
        this.businessId = businessId;
    }

    public BugLog(Integer bugId, String name, Integer type, Integer businessId, String content) {
        this.bugId = bugId;
        this.name = name;
        this.type = type;
        this.businessId = businessId;
        this.content = content;
    }

    public BugLog() {
        super();
    }

    public Integer getBugId() {
        return bugId;
    }

    public void setBugId(Integer bugId) {
        this.bugId = bugId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}