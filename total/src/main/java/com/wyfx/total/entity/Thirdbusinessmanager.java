package com.wyfx.total.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 第三方企业
 */
public class Thirdbusinessmanager {

    private Integer id;//企业id

    private String bid;//企业的uuid

    private String mainAccount;//主账号  未用到

    private String bname;//企业名称(不超过20字)

    private String personInCharge;//负责人

    private String picPhone;//负责人联系电话

    private String address;//企业详细地址

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validityTime;//服务到期时间

    private Integer state; //合作状态{0:合作中;1:暂停合作}

    private Integer deviceNumLimit;//终端账号数量限制  未使用

    private String serverHost;//绑定服务器IP

    private String serverMac;//绑定服务器MAC地址

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date ctime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date utime;

    private String password;//  修改为用来保存license地址

    private String description;//其他说明

    private String longitude;

    private String latitude;


    public Thirdbusinessmanager() {
        super();
    }

    public Thirdbusinessmanager(Integer id, String bid, String mainAccount, String bname, String personInCharge, String picPhone, String address, Date validityTime, Integer state, Integer deviceNumLimit, String serverHost, String serverMac, Date ctime, Date utime, String password, String description, String longitude, String latitude) {
        this.id = id;
        this.bid = bid;
        this.mainAccount = mainAccount;
        this.bname = bname;
        this.personInCharge = personInCharge;
        this.picPhone = picPhone;
        this.address = address;
        this.validityTime = validityTime;
        this.state = state;
        this.deviceNumLimit = deviceNumLimit;
        this.serverHost = serverHost;
        this.serverMac = serverMac;
        this.ctime = ctime;
        this.utime = utime;
        this.password = password;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(String mainAccount) {
        this.mainAccount = mainAccount;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname == null ? null : bname.trim();
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Date getValidityTime() {
        return validityTime;
    }

    public void setValidityTime(Date validityTime) {
        this.validityTime = validityTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getDeviceNumLimit() {
        return deviceNumLimit;
    }

    public void setDeviceNumLimit(Integer deviceNumLimit) {
        this.deviceNumLimit = deviceNumLimit;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost == null ? null : serverHost.trim();
    }

    public String getServerMac() {
        return serverMac;
    }

    public void setServerMac(String serverMac) {
        this.serverMac = serverMac == null ? null : serverMac.trim();
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    @Override
    public String toString() {
        return "Thirdbusinessmanager{" +
                "id=" + id +
                ", bid='" + bid + '\'' +
                ", mainAccount='" + mainAccount + '\'' +
                ", bname='" + bname + '\'' +
                ", personInCharge='" + personInCharge + '\'' +
                ", picPhone='" + picPhone + '\'' +
                ", address='" + address + '\'' +
                ", validityTime=" + validityTime +
                ", state=" + state +
                ", deviceNumLimit=" + deviceNumLimit +
                ", serverHost='" + serverHost + '\'' +
                ", serverMac='" + serverMac + '\'' +
                ", ctime=" + ctime +
                ", utime=" + utime +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}