package com.wyfx.total.entity;

public class Dispatchers {
    private Long did;

    private String userName;

    private String password;

    private String name;

    private Integer status;

    private String businessId;

    private String projectAddress;

    public Dispatchers(Long did, String userName, String password, String name, Integer status, String businessId, String projectAddress) {
        this.did = did;
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.status = status;
        this.businessId = businessId;
        this.projectAddress = projectAddress;
    }

    public Dispatchers() {
        super();
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId == null ? null : businessId.trim();
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress == null ? null : projectAddress.trim();
    }

    @Override
    public String toString() {
        return "Dispatchers{" +
                "did=" + did +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", businessId='" + businessId + '\'' +
                ", projectAddress='" + projectAddress + '\'' +
                '}';
    }
}