package com.wyfx.business.app.vo;

import java.util.Date;

/**
 * @author johnson liu
 * @date 2019/11/15
 * @description 通讯录成员详情Vo
 */
public class MemberDetailVo {

    private Integer id;

    private String name;

    private Integer userType;

    private String workType;

    private String account;

    private String tel;

    private String power;
    /**
     * 主板温度
     */
    private String mainTemperature;
    /**
     * 环境温度
     */
//    private String environmentTemperature;
    private String environmentTemperature;

    /**
     * 气压
     */
    private String pressure;

    private String network;

    private Date time;

    private Double latitude;//纬度

    private Double longitude;//经度

    private Integer onlineStatus;

    private String color;

    public MemberDetailVo() {
    }

    public MemberDetailVo(Integer id, String name, Integer userType, String workType, String account, String tel, String power, String mainTemperature,
                          String environmentTemperature, String pressure, String network, Date time, Double latitude, Double longitude, Integer onlineStatus, String color) {
        this.id = id;
        this.name = name;
        this.userType = userType;
        this.workType = workType;
        this.account = account;
        this.tel = tel;
        this.power = power;
        this.mainTemperature = mainTemperature;
        this.environmentTemperature = environmentTemperature;
        this.pressure = pressure;
        this.network = network;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.onlineStatus = onlineStatus;
        this.color = color;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getMainTemperature() {
        return mainTemperature;
    }

    public void setMainTemperature(String mainTemperature) {
        this.mainTemperature = mainTemperature;
    }

    public String getEnvironmentTemperature() {
        return environmentTemperature;
    }

    public void setEnvironmentTemperature(String environmentTemperature) {
        this.environmentTemperature = environmentTemperature;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
