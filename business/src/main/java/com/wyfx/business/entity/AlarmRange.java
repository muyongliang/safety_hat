package com.wyfx.business.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AlarmRange {
    /**
     * 电子围栏ID
     */
    private Long rid;
    /**
     * 电子围栏名称
     */
    private String name;
    /**
     * 禁止类型{0:禁止进入;1:禁止离开}
     */
    private Integer type;
    /**
     * 报警提示语
     */
    private String tipInfo;

    private Long projectId;
    /**
     * 围栏设置方式{0:鼠标手绘;1:输入4角坐标}
     */
    private Integer rangeType;
    /**
     * 手绘形状{1:圆形;2:矩形;3:多边形}
     */
    private Integer handShape;
    /**
     * 围栏范围(使用JSON数组字符串保存)
     */
    private String gpsRange;

    /**
     * 围栏半径
     */
    private Double radius;

    /**
     * 百度地图围栏ID
     */
    private Integer fenceId;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public AlarmRange() {
    }

    public AlarmRange(Long rid, String name, Integer type, String tipInfo, Long projectId, Integer rangeType, Integer handShape, String gpsRange, Double radius, Integer fenceId, Date createTime, Date updateTime) {
        this.rid = rid;
        this.name = name;
        this.type = type;
        this.tipInfo = tipInfo;
        this.projectId = projectId;
        this.rangeType = rangeType;
        this.handShape = handShape;
        this.gpsRange = gpsRange;
        this.radius = radius;
        this.fenceId = fenceId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
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

    public String getTipInfo() {
        return tipInfo;
    }

    public void setTipInfo(String tipInfo) {
        this.tipInfo = tipInfo == null ? null : tipInfo.trim();
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getRangeType() {
        return rangeType;
    }

    public void setRangeType(Integer rangeType) {
        this.rangeType = rangeType;
    }

    public Integer getHandShape() {
        return handShape;
    }

    public void setHandShape(Integer handShape) {
        this.handShape = handShape;
    }

    public String getGpsRange() {
        return gpsRange;
    }

    public void setGpsRange(String gpsRange) {
        this.gpsRange = gpsRange == null ? null : gpsRange.trim();
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Integer getFenceId() {
        return fenceId;
    }

    public void setFenceId(Integer fenceId) {
        this.fenceId = fenceId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}