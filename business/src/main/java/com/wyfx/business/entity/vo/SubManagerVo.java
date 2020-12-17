package com.wyfx.business.entity.vo;

import com.wyfx.business.entity.Role;

import java.util.Date;
import java.util.List;

/**
 * 子账号管理vo类
 * crate by wsm on 2019-11-29
 */
public class SubManagerVo {

    private Long bid;
    private String name;//姓名
    private String username;//账号
    private String mobile;
    private List<Role> roleList;
    private Integer status;//账号状态 0禁用 1启用
    private String cratePerson;
    private String updatePerson;
    private Date ctime;
    private Date utime;

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCratePerson() {
        return cratePerson;
    }

    public void setCratePerson(String cratePerson) {
        this.cratePerson = cratePerson;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
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

    @Override
    public String toString() {
        return "SubManagerVo{" +
                "bid=" + bid +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", roleList=" + roleList +
                ", status=" + status +
                ", cratePerson='" + cratePerson + '\'' +
                ", updatePerson='" + updatePerson + '\'' +
                ", ctime=" + ctime +
                ", utime=" + utime +
                '}';
    }
}
