package com.wyfx.business.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Gps {
    private Long gid;

    private Long clientId;

    private String power;

    private String mainTemperature;

    private String environmentTemperature;

    private String pressure;

    private String network;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Double longitude;

    private Double latitude;

    private String humidity;

    public Gps(Long gid, Long clientId, String power, String mainTemperature, String environmentTemperature, String pressure, String network, Date createTime, Double longitude, Double latitude, String humidity) {
        this.gid = gid;
        this.clientId = clientId;
        this.power = power;
        this.mainTemperature = mainTemperature;
        this.environmentTemperature = environmentTemperature;
        this.pressure = pressure;
        this.network = network;
        this.createTime = createTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.humidity = humidity;
    }

    public Gps() {
        super();
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power == null ? null : power.trim();
    }

    public String getMainTemperature() {
        return mainTemperature;
    }

    public void setMainTemperature(String mainTemperature) {
        this.mainTemperature = mainTemperature == null ? null : mainTemperature.trim();
    }

    public String getEnvironmentTemperature() {
        return environmentTemperature;
    }

    public void setEnvironmentTemperature(String environmentTemperature) {
        this.environmentTemperature = environmentTemperature == null ? null : environmentTemperature.trim();
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure == null ? null : pressure.trim();
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network == null ? null : network.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}