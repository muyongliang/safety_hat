package com.wyfx.business.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @author johnson liu
 * @date 2019/11/17
 * @description 终端账号查询
 * update by wsm on 2019-11-21
 */
public class ClientAccountSelectVo {

    private Long bid;

    private String password;

    @Excel(name = "账号", orderNum = "1")
    private String userName;

    @Excel(name = "姓名", orderNum = "0")
    private String name;

    @Excel(name = "电话", orderNum = "2")
    private String mobile;

    @Excel(name = "状态状态", replace = {"启用_0", "禁用_1"}, orderNum = "5")
    private Integer status;

    @Excel(name = "工种", orderNum = "3")
    private String workType;

    @Excel(name = "绑定设备编号", orderNum = "4")
    private String number;

    private String account;
    private String tel;
    private Long clientId;
    private Long online_status;
    private Long onlineStatus;//online_status
    private Integer diyFlag;//是否采用系统设置{0:系统默认;1:自定义}
    private String resolution;//视频分辨率



    /*public ClientAccountSelectVo(Long bid, String userName, String name, String mobile, Integer status, String workType, String number) {
        this.bid = bid;
        this.userName = userName;
        this.name = name;
        this.mobile = mobile;
        this.status = status;
        this.workType = workType;
        this.number = number;
    }

    public ClientAccountSelectVo(Long bid, String userName,String password, String name, String mobile, Integer status, String workType, String number) {
        this.bid = bid;
        this.userName = userName;
        this.name = name;
        this.mobile = mobile;
        this.status = status;
        this.workType = workType;
        this.number = number;
        this.password=password;
    }

    public ClientAccountSelectVo(Long bid, String userName,String password, String name, String mobile, Integer status, String workType, String number,Long onlineStatus) {
        this.bid = bid;
        this.userName = userName;
        this.name = name;
        this.mobile = mobile;
        this.status = status;
        this.workType = workType;
        this.number = number;
        this.password=password;
        this.onlineStatus=onlineStatus;
    }*/


    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
        this.clientId = bid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.account = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
        this.tel = mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getOnline_status() {
        return online_status;
    }

    public void setOnline_status(Long online_status) {
        this.online_status = online_status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Long onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Integer getDiyFlag() {
        return diyFlag;
    }

    public void setDiyFlag(Integer diyFlag) {
        this.diyFlag = diyFlag;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override
    public String toString() {
        return "ClientAccountSelectVo{" +
                "bid=" + bid +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", status=" + status +
                ", workType='" + workType + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
