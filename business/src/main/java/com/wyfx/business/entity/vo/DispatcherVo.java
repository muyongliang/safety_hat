package com.wyfx.business.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @author johnson liu
 * @date 2019/11/14
 * @description 调度员Vo类
 * update by wsm on 2019-11-22
 */
public class DispatcherVo implements Serializable {
    private Long bid;

    private String password;

    @Excel(name = "账号", orderNum = "0")
    private String userName;

    @Excel(name = "姓名", orderNum = "1")
    private String name;

    @Excel(name = "电话", orderNum = "2")
    private String mobile;

    @Excel(name = "账号状态", orderNum = "3", replace = {"启用_0", "禁用_1"})
    private Integer status;

    public DispatcherVo(Long bid, String userName, String name, String mobile, Integer status) {
        this.bid = bid;
        this.userName = userName;
        this.name = name;
        this.mobile = mobile;
        this.status = status;
    }

    public DispatcherVo(Long bid, String password, String userName, String name, String mobile, Integer status) {
        this.bid = bid;
        this.password = password;
        this.userName = userName;
        this.name = name;
        this.mobile = mobile;
        this.status = status;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "DispatcherVo{" +
                "bid=" + bid +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", status=" + status +
                '}';
    }
}
