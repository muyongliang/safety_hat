package com.wyfx.total.entity;

public class ClientInfos {
    private Long cid;

    private String account;

    private String name;

    private Integer projectId;

    private Integer onlineFlag;

    private String businessId;

    private String projectAddress;

    public ClientInfos(Long cid, String account, String name, Integer projectId, Integer onlineFlag, String businessId, String projectAddress) {
        this.cid = cid;
        this.account = account;
        this.name = name;
        this.projectId = projectId;
        this.onlineFlag = onlineFlag;
        this.businessId = businessId;
        this.projectAddress = projectAddress;
    }

    public ClientInfos() {
        super();
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getOnlineFlag() {
        return onlineFlag;
    }

    public void setOnlineFlag(Integer onlineFlag) {
        this.onlineFlag = onlineFlag;
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
        return "ClientInfos{" +
                "cid=" + cid +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", projectId=" + projectId +
                ", onlineFlag=" + onlineFlag +
                ", businessId='" + businessId + '\'' +
                ", projectAddress='" + projectAddress + '\'' +
                '}';
    }
}