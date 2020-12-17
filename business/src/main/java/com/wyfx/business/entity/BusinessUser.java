package com.wyfx.business.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BusinessUser implements Serializable {
    // 用户状态：锁定
    public static final int STATUS_LOCK = 0;
    // 用户状态：有效
    public static final int STATUS_VALID = 1;
    private Long bid;

    @NotBlank(message = "用户名不能为空")
    @NotNull
    private String userName;
    @NotNull
    @NotBlank(message = "密码不能为空")
    private String password;

    private String name;

    private String mobile;

    private String createPerson;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;

    private String modifyPerson;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;
    /**
     * 状态{0:锁定;1有效:}
     */
    private Integer status;

    private Long projectId;

    private String token;

    private Long businessId;
    /**
     * 账号类型{0:调度员,1:企业管理员;2:终端账号;3:其他管理员账号}
     */
    private Integer userType;
    /**
     * 在线状态{0:离线；1:在线;2:手机在线;3:电脑在线;4:电脑同时}
     */
    private Integer onlineStatus;
    /**
     * 配置调度员的字典id,显示色值
     */
    private Long zidianId;

    private List<Role> roles = new ArrayList<>();

    public BusinessUser(Long bid, String userName, String password, String name, String mobile, String createPerson, Date createTime, Date modifyTime, String modifyPerson, Date lastLoginTime, Integer status,
                        Long projectId, String token, Long businessId, Integer userType, Integer onlineStatus, Long zidianId) {
        this.bid = bid;
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.mobile = mobile;
        this.createPerson = createPerson;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.modifyPerson = modifyPerson;
        this.lastLoginTime = lastLoginTime;
        this.status = status;
        this.projectId = projectId;
        this.token = token;
        this.businessId = businessId;
        this.userType = userType;
        this.onlineStatus = onlineStatus;
        this.zidianId = zidianId;
    }

    public BusinessUser() {
        super();
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
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyPerson() {
        return modifyPerson;
    }

    public void setModifyPerson(String modifyPerson) {
        this.modifyPerson = modifyPerson == null ? null : modifyPerson.trim();
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = (status == null) ? 0 : status;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
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

    public Long getZidianId() {
        return zidianId;
    }

    public void setZidianId(Long zidianId) {
        this.zidianId = zidianId;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "BusinessUser{" +
                "bid=" + bid +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", createPerson='" + createPerson + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", modifyPerson='" + modifyPerson + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", status=" + status +
                ", projectId=" + projectId +
                ", token='" + token + '\'' +
                ", businessId=" + businessId +
                ", userType=" + userType +
                ", onlineStatus=" + onlineStatus +
                ", zidianId=" + zidianId +
                '}';
    }
}