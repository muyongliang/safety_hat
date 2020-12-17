package com.wyfx.business.entity.vo;

public class MemberBeanVo {
    private String accountType;
    private String headUrl;
    private String id;
    private String name;
    private String state;

    public MemberBeanVo(String accountType, String headUrl, String id, String name, String state) {
        this.accountType = accountType;
        this.headUrl = headUrl;
        this.id = id;
        this.name = name;
        this.state = state;
    }

    public MemberBeanVo() {
    }

    @Override
    public String toString() {
        return "MemberBeanVo{" +
                "accountType='" + accountType + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
