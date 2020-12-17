package com.wyfx.total.entity.vo;


/**
 * crete by wsm on 2019-12-3
 * 总后台查询终端账号信息 vo类
 */
public class MyClientAccountVo {

    private Long clientId;

    private String bname;//企业名

    private Integer onlineStatus;//终端在线状态

    private String name;//姓名

    private String username;//账号名

    private String mobile;

    private String workType;

    private String number;

    private Integer clientStatus;//终端使用状态

    private Integer pageNum;

    private Integer pageSize;

    public MyClientAccountVo() {//建议添加无参构造 否则mybatis会出现无法确定参数类型的错误
    }

    public MyClientAccountVo(Long clientId, String bname, Integer onlineStatus, String name, String username, String mobile, String workType, String number, Integer clientStatus, Integer pageNum, Integer pageSize) {
        this.clientId = clientId;
        this.bname = bname;
        this.onlineStatus = onlineStatus;
        this.name = name;
        this.username = username;
        this.mobile = mobile;
        this.workType = workType;
        this.number = number;
        this.clientStatus = clientStatus;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
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

    public Integer getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(Integer clientStatus) {
        this.clientStatus = clientStatus;
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

    @Override
    public String toString() {
        return "MyClientAccountVo{" +
                "clientId=" + clientId +
                ", bname='" + bname + '\'' +
                ", onlineStatus=" + onlineStatus +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", workType='" + workType + '\'' +
                ", number='" + number + '\'' +
                ", clientStatus=" + clientStatus +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}