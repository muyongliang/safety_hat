package com.wyfx.business.entity.vo;

/**
 * @author johnson liu
 * @date 2019/11/8
 * @description 终端账号Vo类
 */
public class ClientAccountVo {
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

    private Long endBinding;

    private Integer diyFlag;

    private Long projectId;

    public Long getEndBinding() {
        return endBinding;
    }

    public void setEndBinding(Long endBinding) {
        this.endBinding = endBinding;
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
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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
        this.resolution = resolution;
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

    public Integer getDiyFlag() {
        return diyFlag;
    }

    public void setDiyFlag(Integer diyFlag) {
        this.diyFlag = diyFlag;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "ClientAccountVo{" +
                "clientId=" + clientId +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", workId=" + workId +
                ", resolution='" + resolution + '\'' +
                ", minKps=" + minKps +
                ", maxKps=" + maxKps +
                ", deviceId=" + deviceId +
                ", diyFlag=" + diyFlag +
                ", projectId=" + projectId +
                '}';
    }
}
