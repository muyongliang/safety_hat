package com.wyfx.business.entity;

public class AppUpdateSetting {
    private Long appId;

    private String currentVersion;

    private String lastVersion;

    private Integer updateSet;//更新设置 更新设置 1自动更新 2手动更新

    public AppUpdateSetting(Long appId, String currentVersion, String lastVersion, Integer updateSet) {
        this.appId = appId;
        this.currentVersion = currentVersion;
        this.lastVersion = lastVersion;
        this.updateSet = updateSet;
    }

    public AppUpdateSetting() {
        super();
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion == null ? null : currentVersion.trim();
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion == null ? null : lastVersion.trim();
    }

    public Integer getUpdateSet() {
        return updateSet;
    }

    public void setUpdateSet(Integer updateSet) {
        this.updateSet = updateSet;
    }


    @Override
    public String toString() {
        return "AppUpdateSetting{" +
                "appId=" + appId +
                ", currentVersion='" + currentVersion + '\'' +
                ", lastVersion='" + lastVersion + '\'' +
                ", updateSet=" + updateSet +
                '}';
    }
}