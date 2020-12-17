package com.wyfx.total.entity;

public class DefaultSetting {
    private Long id;

    private Integer vedioTimeLimit; //无限制提交0 有限制提交限制值

    private Integer isAutoUpload;

    private Integer dispatcherUploadLimit;

    private Integer appLog;//0 不自动清理日志 其他为清理日志的天数

    private Integer dispatcherLog;

    private Integer tipTime;

    private Integer thirdTipTime;

    private Double storeTip;

    public DefaultSetting(Long id, Integer vedioTimeLimit, Integer isAutoUpload, Integer dispatcherUploadLimit, Integer appLog, Integer dispatcherLog, Integer tipTime, Integer thirdTipTime, Double storeTip) {
        this.id = id;
        this.vedioTimeLimit = vedioTimeLimit;
        this.isAutoUpload = isAutoUpload;
        this.dispatcherUploadLimit = dispatcherUploadLimit;
        this.appLog = appLog;
        this.dispatcherLog = dispatcherLog;
        this.tipTime = tipTime;
        this.thirdTipTime = thirdTipTime;
        this.storeTip = storeTip;
    }

    public DefaultSetting() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVedioTimeLimit() {
        return vedioTimeLimit;
    }

    public void setVedioTimeLimit(Integer vedioTimeLimit) {
        this.vedioTimeLimit = vedioTimeLimit;
    }

    public Integer getIsAutoUpload() {
        return isAutoUpload;
    }

    public void setIsAutoUpload(Integer isAutoUpload) {
        this.isAutoUpload = isAutoUpload;
    }

    public Integer getDispatcherUploadLimit() {
        return dispatcherUploadLimit;
    }

    public void setDispatcherUploadLimit(Integer dispatcherUploadLimit) {
        this.dispatcherUploadLimit = dispatcherUploadLimit;
    }

    public Integer getAppLog() {
        return appLog;
    }

    public void setAppLog(Integer appLog) {
        this.appLog = appLog;
    }

    public Integer getDispatcherLog() {
        return dispatcherLog;
    }

    public void setDispatcherLog(Integer dispatcherLog) {
        this.dispatcherLog = dispatcherLog;
    }

    public Integer getTipTime() {
        return tipTime;
    }

    public void setTipTime(Integer tipTime) {
        this.tipTime = tipTime;
    }

    public Integer getThirdTipTime() {
        return thirdTipTime;
    }

    public void setThirdTipTime(Integer thirdTipTime) {
        this.thirdTipTime = thirdTipTime;
    }

    public Double getStoreTip() {
        return storeTip;
    }

    public void setStoreTip(Double storeTip) {
        this.storeTip = storeTip;
    }

    @Override
    public String toString() {
        return "DefaultSetting{" +
                "id=" + id +
                ", vedioTimeLimit=" + vedioTimeLimit +
                ", isAutoUpload=" + isAutoUpload +
                ", dispatcherUploadLimit=" + dispatcherUploadLimit +
                ", appLog=" + appLog +
                ", dispatcherLog=" + dispatcherLog +
                ", tipTime=" + tipTime +
                ", thirdTipTime=" + thirdTipTime +
                ", storeTip=" + storeTip +
                '}';
    }
}