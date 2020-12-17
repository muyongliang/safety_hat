package com.wyfx.business.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class MenuManager {
    private Long mid;

    private Long parentId;

    private String name;

    private String url;

    private Integer type;

    private Integer orderNum;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;

    private String remark;

    public MenuManager(Long mid, Long parentId, String name, String url, Integer type, Integer orderNum, Date createTime, Date modifyTime) {
        this.mid = mid;
        this.parentId = parentId;
        this.name = name;
        this.url = url;
        this.type = type;
        this.orderNum = orderNum;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
    }

    public MenuManager(Long mid, Long parentId, String name, Integer type, Integer orderNum) {
        this.mid = mid;
        this.parentId = parentId;
        this.name = name;
        this.type = type;
        this.orderNum = orderNum;
    }

    public MenuManager(Long mid, Long parentId, String name, String url, Integer type, Integer orderNum, Date createTime, Date modifyTime, String remark) {
        this.mid = mid;
        this.parentId = parentId;
        this.name = name;
        this.url = url;
        this.type = type;
        this.orderNum = orderNum;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.remark = remark;
    }

    public MenuManager() {
        super();
    }

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}