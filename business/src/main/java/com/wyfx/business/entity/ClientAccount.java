package com.wyfx.business.entity;

public class ClientAccount {
    private Long clientId;

    private String account;

    private String password;

    private String name;

    private String tel;

    private Long workId;

    private String resolution;

    private Integer minKps;

    private Integer maxKps;

    private Long deviceId;

    private Long projectId;

    private Long businessId;
    /**
     * 是否采用系统设置{0:系统默认;1:自定义}
     */
    private Integer diyFlag;
    /**
     * 账号启用状态{0:启用;1:禁用}
     */
    private Integer status;

    private Long bid;

    public ClientAccount(Long clientId, String account, String password, String name, String tel, Long workId, String resolution, Integer minKps, Integer maxKps, Long deviceId, Long projectId, Long businessId, Integer diyFlag, Integer status, Long bid) {
        this.clientId = clientId;
        this.account = account;
        this.password = password;
        this.name = name;
        this.tel = tel;
        this.workId = workId;
        this.resolution = resolution;
        this.minKps = minKps;
        this.maxKps = maxKps;
        this.deviceId = deviceId;
        this.projectId = projectId;
        this.businessId = businessId;
        this.diyFlag = diyFlag;
        this.status = status;
        this.bid = bid;
    }

    public ClientAccount() {
        super();
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution == null ? null : resolution.trim();
    }

    public Integer getMinKps() {
        return minKps;
    }

    public void setMinKps(Integer minKps) {
        this.minKps = minKps;
    }

    public Integer getMaxKps() {
        return maxKps;
    }

    public void setMaxKps(Integer maxKps) {
        this.maxKps = maxKps;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Integer getDiyFlag() {
        return diyFlag;
    }

    public void setDiyFlag(Integer diyFlag) {
        this.diyFlag = diyFlag;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }
}