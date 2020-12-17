package com.wyfx.business.entity.vo;

/**
 * @author johnson liu
 * @date 2019/11/23
 * @description 成员信息Vo
 */
public class MemberInfoVo {

    private Long bid;

    /**
     * 账号
     */
    private String userName;
    /**
     * 姓名
     */
    private String name;
    /**
     * 电话
     */
    private String mobile;

    /**
     * 工种名称
     */
    private String workName;

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

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }
}
