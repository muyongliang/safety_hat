package com.wyfx.total.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 企业信息表
 */
@ApiModel()
public class BusinessManager implements Serializable {
    @ApiModelProperty(hidden = true)
    private Integer id;//企业ID

    @ApiModelProperty(hidden = true)
    private String bid;//企业的UUID或者是账号和密码结合生成的加密字符串

    @ApiModelProperty(value = "企业名字", name = "bname", required = true)
    private String bname;//企业名称(不超过20字)

    @ApiModelProperty(value = "企业主账号", name = "mainAccount", required = true)
    private String mainAccount;//企业主账号

    @ApiModelProperty(value = "负责人", name = "personInCharge", required = true)
    private String personInCharge;//负责人

    @ApiModelProperty(value = "负责人联系电话", name = "picPhone", required = true)
    private String picPhone;//负责人联系电话

    @ApiModelProperty(value = "企业详细地址", name = "address", required = true)
    private String address;//企业详细地址

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "服务到期时间", name = "validityTime", required = true)
    private Date validityTime;//服务到期时间

    @ApiModelProperty(value = "合作状态{0:合作中;1:暂停合作}", name = "status", required = true)
    private Integer status;//合作状态{0:合作中;1:暂停合作}

    @ApiModelProperty(value = "终端账号数量限制", name = "deviceNumLimit", required = true)
    private Integer deviceNumLimit;//终端账号数量限制

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间", name = "ctime", required = false, hidden = true)
    private Date ctime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间", name = "utime", required = false, hidden = true)
    private Date utime;

    @ApiModelProperty(value = "企业管理员账号密码", name = "password", required = false, hidden = true)
    private String password;//企业管理员账号密码

    @ApiModelProperty(value = "其他说明", name = "description", required = false)
    private String description;//其他说明

    @ApiModelProperty(value = "经度", name = "longitude", required = true)
    private String longitude; //经度

    @ApiModelProperty(value = "维度", name = "latitude", required = true)
    private String latitude;// 维度

    public BusinessManager(Integer id, String bid, String bname, String mainAccount, String personInCharge, String picPhone, String address, Date validityTime, Integer status,
                           Integer deviceNumLimit, Date ctime, Date utime, String password, String longitude, String latitude) {
        this.id = id;
        this.bid = bid;
        this.bname = bname;
        this.mainAccount = mainAccount;
        this.personInCharge = personInCharge;
        this.picPhone = picPhone;
        this.address = address;
        this.validityTime = validityTime;
        this.status = status;
        this.deviceNumLimit = deviceNumLimit;
        this.ctime = ctime;
        this.utime = utime;
        this.password = password;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public BusinessManager(Integer id, String bid, String bname, String mainAccount, String personInCharge,
                           String picPhone, String address, Date validityTime, Integer status,
                           Integer deviceNumLimit, Date ctime, Date utime, String password, String description, String longitude, String latitude) {
        this.id = id;
        this.bid = bid;
        this.bname = bname;
        this.mainAccount = mainAccount;
        this.personInCharge = personInCharge;
        this.picPhone = picPhone;
        this.address = address;
        this.validityTime = validityTime;
        this.status = status;
        this.deviceNumLimit = deviceNumLimit;
        this.ctime = ctime;
        this.utime = utime;
        this.password = password;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public BusinessManager() {
        super();
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
        this.bid = bid == null ? null : bid.trim();
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname == null ? null : bname.trim();
    }

    public String getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(String mainAccount) {
        this.mainAccount = mainAccount == null ? null : mainAccount.trim();
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeviceNumLimit() {
        return deviceNumLimit;
    }

    public void setDeviceNumLimit(Integer deviceNumLimit) {
        this.deviceNumLimit = deviceNumLimit;
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

    @Override
    public String toString() {
        return "BusinessManager{" +
                "id=" + id +
                ", bid='" + bid + '\'' +
                ", bname='" + bname + '\'' +
                ", mainAccount='" + mainAccount + '\'' +
                ", personInCharge='" + personInCharge + '\'' +
                ", picPhone='" + picPhone + '\'' +
                ", address='" + address + '\'' +
                ", validityTime=" + validityTime +
                ", status=" + status +
                ", deviceNumLimit=" + deviceNumLimit +
                ", ctime=" + ctime +
                ", utime=" + utime +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BusinessManager that = (BusinessManager) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(bid, that.bid) &&
                Objects.equals(bname, that.bname) &&
                Objects.equals(mainAccount, that.mainAccount) &&
                Objects.equals(personInCharge, that.personInCharge) &&
                Objects.equals(picPhone, that.picPhone) &&
                Objects.equals(address, that.address) &&
                Objects.equals(validityTime, that.validityTime) &&
                Objects.equals(status, that.status) &&
                Objects.equals(deviceNumLimit, that.deviceNumLimit) &&
                Objects.equals(ctime, that.ctime) &&
                Objects.equals(utime, that.utime) &&
                Objects.equals(password, that.password) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bid, bname, mainAccount, personInCharge, picPhone, address, validityTime, status, deviceNumLimit, ctime, utime, password, description);
    }
}