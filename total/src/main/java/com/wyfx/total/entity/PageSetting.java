package com.wyfx.total.entity;

public class PageSetting {
    private Integer pid;

    private String systemName;

    private String logoUrl;

    private String metaUrl;

    public PageSetting(Integer pid, String systemName, String logoUrl, String metaUrl) {
        this.pid = pid;
        this.systemName = systemName;
        this.logoUrl = logoUrl;
        this.metaUrl = metaUrl;
    }

    public PageSetting() {
        super();
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName == null ? null : systemName.trim();
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl == null ? null : logoUrl.trim();
    }

    public String getMetaUrl() {
        return metaUrl;
    }

    public void setMetaUrl(String metaUrl) {
        this.metaUrl = metaUrl == null ? null : metaUrl.trim();
    }
}