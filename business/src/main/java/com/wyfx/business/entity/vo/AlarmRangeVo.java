package com.wyfx.business.entity.vo;

import com.wyfx.business.alarmRange.model.LatLng;

import java.util.List;

/**
 * @author johnson liu
 * @date 2019/11/28
 * @description 电子围栏Vo
 */
public class AlarmRangeVo {

    private Long rid;

    private String name;

    private Integer type;

    private String tipInfo;

    private Long projectId;

    private Integer rangeType;

    private Integer handShape;

    private List<LatLng> gpsRange;

    private Double radius;//围栏半径

    private Integer fenceId;//百度地图围栏ID

    public AlarmRangeVo() {
    }

    public AlarmRangeVo(Long rid, String name, Integer type, String tipInfo, Long projectId, Integer rangeType, Integer handShape, List<LatLng> gpsRange, Double radius) {
        this.rid = rid;
        this.name = name;
        this.type = type;
        this.tipInfo = tipInfo;
        this.projectId = projectId;
        this.rangeType = rangeType;
        this.handShape = handShape;
        this.gpsRange = gpsRange;
        this.radius = radius;
    }

    public AlarmRangeVo(Long rid, String name, Integer type, String tipInfo, Long projectId, Integer rangeType, Integer handShape, List<LatLng> gpsRange, Double radius, Integer fenceId) {
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
        this.name = name;
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
        this.tipInfo = tipInfo;
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

    public List<LatLng> getGpsRange() {
        return gpsRange;
    }

    public void setGpsRange(List<LatLng> gpsRange) {
        this.gpsRange = gpsRange;
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
}
