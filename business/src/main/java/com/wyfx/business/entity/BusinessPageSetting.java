package com.wyfx.business.entity;

public class BusinessPageSetting {
    private String pid;

    private String systemName;

    private Long businessId;

    private String logoImg;

    private String metaImg;

    public BusinessPageSetting(String pid, String systemName, Long businessId, String logoImg, String metaImg) {
        this.pid = pid;
        this.systemName = systemName;
        this.businessId = businessId;
        this.logoImg = logoImg;
        this.metaImg = metaImg;
    }

    public BusinessPageSetting() {
        super();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName == null ? null : systemName.trim();
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(String logoImg) {
        this.logoImg = logoImg;
    }

    public String getMetaImg() {
        return metaImg;
    }

    public void setMetaImg(String metaImg) {
        this.metaImg = metaImg;
    }
}