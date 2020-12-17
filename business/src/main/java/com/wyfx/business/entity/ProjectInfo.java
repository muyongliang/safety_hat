package com.wyfx.business.entity;

public class ProjectInfo {
    private Long pid;

    private String projectName;

    private String projectAddress;

    private String personInCharge;

    private String picPhone;

    private Integer status;

    private String introduction;

    private Long businessId;

    private String longitude;//经度

    private String latitude;  //纬度

    private Integer altitude = 500;//海拔

    public ProjectInfo(Long pid, String projectName, String projectAddress, String personInCharge, String picPhone, Integer status, String introduction, Long businessId, String longitude, String latitude) {
        this.pid = pid;
        this.projectName = projectName;
        this.projectAddress = projectAddress;
        this.personInCharge = personInCharge;
        this.picPhone = picPhone;
        this.status = status;
        this.introduction = introduction;
        this.businessId = businessId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public ProjectInfo(Long pid, String projectName, String projectAddress, String personInCharge, String picPhone, Integer status, String introduction, Long businessId, String longitude, String latitude, Integer altitude) {
        this.pid = pid;
        this.projectName = projectName;
        this.projectAddress = projectAddress;
        this.personInCharge = personInCharge;
        this.picPhone = picPhone;
        this.status = status;
        this.introduction = introduction;
        this.businessId = businessId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    public ProjectInfo() {
        super();
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress == null ? null : projectAddress.trim();
    }

    public String getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge == null ? null : personInCharge.trim();
    }

    public String getPicPhone() {
        return picPhone;
    }

    public void setPicPhone(String picPhone) {
        this.picPhone = picPhone == null ? null : picPhone.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Integer getAltitude() {
        return altitude;
    }

    public void setAltitude(Integer altitude) {
        this.altitude = altitude;
    }
}