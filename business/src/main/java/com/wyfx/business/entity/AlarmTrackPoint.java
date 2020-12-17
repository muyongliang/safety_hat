package com.wyfx.business.entity;

import java.util.Date;

public class AlarmTrackPoint {
    private Long tid;

    private Double longitude;

    private Double latitude;

    private Integer radius;

    private String coordType;

    private Date locTime;

    private Date createTime;

    public AlarmTrackPoint(Long tid, Double longitude, Double latitude, Integer radius, String coordType, Date locTime, Date createTime) {
        this.tid = tid;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
        this.coordType = coordType;
        this.locTime = locTime;
        this.createTime = createTime;
    }

    public AlarmTrackPoint() {
        super();
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public String getCoordType() {
        return coordType;
    }

    public void setCoordType(String coordType) {
        this.coordType = coordType == null ? null : coordType.trim();
    }

    public Date getLocTime() {
        return locTime;
    }

    public void setLocTime(Date locTime) {
        this.locTime = locTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}