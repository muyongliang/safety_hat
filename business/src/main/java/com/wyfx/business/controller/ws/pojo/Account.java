package com.wyfx.business.controller.ws.pojo;

/**
 * @author johnson liu
 * @date 2019/12/7
 * @description
 */
public class Account {

    private String accountType;
    private String headUrl;
    /**
     * id : 123
     * name : 张三
     * headUrl : https://pic1.zhimg.com/v2-fda399250493e674f2152c581490d6eb_1200x500.jpg
     * state : 1
     * accountType : z
     */

    private String id;
    private String name;
    /**
     * 1:同意;0:拒绝
     */
    private String state;

    public Account() {
    }

    public Account(String accountType, String headUrl, String id, String name, String state) {
        this.accountType = accountType;
        this.headUrl = headUrl;
        this.id = id;
        this.name = name;
        this.state = state;
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

    @Override
    public String toString() {
        return "Account{" +
                "accountType='" + accountType + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
