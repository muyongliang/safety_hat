package com.wyfx.business.app.vo;

/**
 * @author johnson liu
 * @date 2019/11/14
 * @description app通讯录VO
 */

public class ContactsVo {

    private Integer id;
    private String userName;

    /**
     * 姓名
     */
    private String fullName;

    private String mobile;

    /**
     * 在线状态
     */
    private Integer status;

    /**
     * 用户类型{1:调度员;2:终端用户}
     */
    private Integer type;

    /**
     * 工种名称
     */
    private String workName;

    private String color;

    private String tname;

    public ContactsVo(Integer id, String userName, String fullName, String mobile, Integer status, Integer type, String workName, String color, String tname) {
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
        this.mobile = mobile;
        this.status = status;
        this.type = type;
        this.workName = workName;
        this.color = color;
        this.tname = tname;
    }

    public ContactsVo() {
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
        this.status = (status == null) ? 0 : status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

}
