package com.wyfx.total.entity.vo;


import java.io.Serializable;

/**
 * create by wsm on 2019-12-4
 * 远程调用接口vo类
 */
public class MyDispatcherVo implements Serializable {

    private Long bid;

    private String businessName;//企业名

    private String userName;//账号

    private String name;//名字

    private String mobile;

    private Integer accountStatus;//账号状态 禁用与否

    private Integer onlineStatus;//在线状态 在线与否

    private Integer pageNum;

    private Integer pageSize;


    public MyDispatcherVo(Long bid, String businessName, String userName, String name, String mobile, Integer accountStatus, Integer onlineStatus, Integer pageNum, Integer pageSize) {
        this.bid = bid;
        this.businessName = businessName;
        this.userName = userName;
        this.name = name;
        this.mobile = mobile;
        this.accountStatus = accountStatus;
        this.onlineStatus = onlineStatus;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public MyDispatcherVo() {
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
    }

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }


    @Override
    public String toString() {
        return "MyDispatcherVo{" +
                "bid=" + bid +
                ", businessName='" + businessName + '\'' +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", accountStatus=" + accountStatus +
                ", onlineStatus=" + onlineStatus +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
