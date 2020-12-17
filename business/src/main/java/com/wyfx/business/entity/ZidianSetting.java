package com.wyfx.business.entity;

import java.util.Date;

public class ZidianSetting {
    private Long zid;

    private Long zidianType;

    private String name;

    private Integer status;

    private String color;

    private Integer orderNum;

    private Long businessId;

    private Date updateTime;
    private Integer maxOrderNum;

    public ZidianSetting(Long zid, Long zidianType, String name, Integer status, String color, Integer orderNum, Long businessId, Date updateTime) {
        this.zid = zid;
        this.zidianType = zidianType;
        this.name = name;
        this.status = status;
        this.color = color;
        this.orderNum = orderNum;
        this.businessId = businessId;
        this.updateTime = updateTime;
    }

    public ZidianSetting(Long zidianType, String name, Integer status, String color, Long businessId) {
        this.zidianType = zidianType;
        this.name = name;
        this.status = status;
        this.color = color;
        this.businessId = businessId;
    }

    public ZidianSetting() {
        super();
    }

    public Long getZid() {
        return zid;
    }

    public void setZid(Long zid) {
        this.zid = zid;
    }

    public Long getZidianType() {
        return zidianType;
    }

    public void setZidianType(Long zidianType) {
        this.zidianType = zidianType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getMaxOrderNum() {
        return maxOrderNum;
    }

    public void setMaxOrderNum(Integer maxOrderNum) {
        this.maxOrderNum = maxOrderNum;
    }
}