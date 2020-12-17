package com.wyfx.total.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * create by wsm on 2019-12-12
 * 企业后台获取企业设置 vo类
 */
public class DiySetVo {

    private String token;

    private Integer deviceNumLimit;//终端账号数量限制

    private String mainAccount;//企业的主账号

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validityTime;//到期时间

    private Integer isCall;//是否支持语音通话 0支持，1不支持

    private Integer talkback;//是否支持对讲  0支持，1不支持

    private Integer vedio; //是否支持视频通话  0支持，1不支持

    private Integer broadcast; //是否支持广播  0支持，1不支持

    private Integer mapRange;//是否支持电子围栏  0支持，1不支持

    private Integer trail;//轨迹  0支持，1不支持

    private Integer listener;//无声侦护  0支持，1不支持

    private Integer vedioTimeLimit;//终端视频录制时长限制(单位:分钟)

    private Integer vedioTimeLimitSys;//终端视频录制时长状态{0:跟随系统配置;1:无限制;2:限制}

    private Integer isAutoUpload;//终端视频是否自动上传(单位:M)

    private Integer isAutoUploadSys;//终端视频自动上传状态{0:跟随系统配置;1:无限制;2:限制}

    private Integer dispatcherUploadLimit;//调度员视频自动上传限制(单位:M)

    private Integer dispatcherUploadLimitSys;//调度员app视频自动上传状态{0:跟随系统配置;1:关2开}

    private Integer appLog;//终端日志记录开关(几天后自动清理日志)

    private Integer appLogSys;//终端日志记录状态{0:跟随系统配置;1:关2开}

    private Integer dispatcherLog;//调度员日志记录开关(几天后自动清理日志)

    private Integer dispatcherLogSys;//调度员app日志记录状态{0:跟随系统配置;1:关2开}

    private Integer storeTip;//企业存储空间报警设置

    private Integer storeTipSys;//企业存储空间报警配置{0:跟随系统配置;1:自定义;}

    public DiySetVo() {
        super();
    }

    public DiySetVo(String token, Integer deviceNumLimit, String mainAccount, Date validityTime, Integer isCall, Integer talkback, Integer vedio, Integer broadcast, Integer mapRange, Integer trail, Integer listener, Integer vedioTimeLimit, Integer vedioTimeLimitSys, Integer isAutoUpload, Integer isAutoUploadSys, Integer dispatcherUploadLimit, Integer dispatcherUploadLimitSys, Integer appLog, Integer appLogSys, Integer dispatcherLog, Integer dispatcherLogSys, Integer storeTip, Integer storeTipSys) {
        this.token = token;
        this.deviceNumLimit = deviceNumLimit;
        this.mainAccount = mainAccount;
        this.validityTime = validityTime;
        this.isCall = isCall;
        this.talkback = talkback;
        this.vedio = vedio;
        this.broadcast = broadcast;
        this.mapRange = mapRange;
        this.trail = trail;
        this.listener = listener;
        this.vedioTimeLimit = vedioTimeLimit;
        this.vedioTimeLimitSys = vedioTimeLimitSys;
        this.isAutoUpload = isAutoUpload;
        this.isAutoUploadSys = isAutoUploadSys;
        this.dispatcherUploadLimit = dispatcherUploadLimit;
        this.dispatcherUploadLimitSys = dispatcherUploadLimitSys;
        this.appLog = appLog;
        this.appLogSys = appLogSys;
        this.dispatcherLog = dispatcherLog;
        this.dispatcherLogSys = dispatcherLogSys;
        this.storeTip = storeTip;
        this.storeTipSys = storeTipSys;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getDeviceNumLimit() {
        return deviceNumLimit;
    }

    public void setDeviceNumLimit(Integer deviceNumLimit) {
        this.deviceNumLimit = deviceNumLimit;
    }

    public Integer getIsCall() {
        return isCall;
    }

    public void setIsCall(Integer isCall) {
        this.isCall = isCall;
    }

    public Integer getTalkback() {
        return talkback;
    }

    public void setTalkback(Integer talkback) {
        this.talkback = talkback;
    }

    public Integer getVedio() {
        return vedio;
    }

    public void setVedio(Integer vedio) {
        this.vedio = vedio;
    }

    public Integer getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(Integer broadcast) {
        this.broadcast = broadcast;
    }

    public Integer getMapRange() {
        return mapRange;
    }

    public void setMapRange(Integer mapRange) {
        this.mapRange = mapRange;
    }

    public Integer getTrail() {
        return trail;
    }

    public void setTrail(Integer trail) {
        this.trail = trail;
    }

    public Integer getListener() {
        return listener;
    }

    public void setListener(Integer listener) {
        this.listener = listener;
    }

    public Integer getVedioTimeLimit() {
        return vedioTimeLimit;
    }

    public void setVedioTimeLimit(Integer vedioTimeLimit) {
        this.vedioTimeLimit = vedioTimeLimit;
    }

    public Integer getVedioTimeLimitSys() {
        return vedioTimeLimitSys;
    }

    public void setVedioTimeLimitSys(Integer vedioTimeLimitSys) {
        this.vedioTimeLimitSys = vedioTimeLimitSys;
    }

    public Integer getIsAutoUpload() {
        return isAutoUpload;
    }

    public void setIsAutoUpload(Integer isAutoUpload) {
        this.isAutoUpload = isAutoUpload;
    }

    public Integer getIsAutoUploadSys() {
        return isAutoUploadSys;
    }

    public void setIsAutoUploadSys(Integer isAutoUploadSys) {
        this.isAutoUploadSys = isAutoUploadSys;
    }

    public Integer getDispatcherUploadLimit() {
        return dispatcherUploadLimit;
    }

    public void setDispatcherUploadLimit(Integer dispatcherUploadLimit) {
        this.dispatcherUploadLimit = dispatcherUploadLimit;
    }

    public Integer getDispatcherUploadLimitSys() {
        return dispatcherUploadLimitSys;
    }

    public void setDispatcherUploadLimitSys(Integer dispatcherUploadLimitSys) {
        this.dispatcherUploadLimitSys = dispatcherUploadLimitSys;
    }

    public Integer getAppLog() {
        return appLog;
    }

    public void setAppLog(Integer appLog) {
        this.appLog = appLog;
    }

    public Integer getAppLogSys() {
        return appLogSys;
    }

    public void setAppLogSys(Integer appLogSys) {
        this.appLogSys = appLogSys;
    }

    public Integer getDispatcherLog() {
        return dispatcherLog;
    }

    public void setDispatcherLog(Integer dispatcherLog) {
        this.dispatcherLog = dispatcherLog;
    }

    public Integer getDispatcherLogSys() {
        return dispatcherLogSys;
    }

    public void setDispatcherLogSys(Integer dispatcherLogSys) {
        this.dispatcherLogSys = dispatcherLogSys;
    }

    public Integer getStoreTip() {
        return storeTip;
    }

    public void setStoreTip(Integer storeTip) {
        this.storeTip = storeTip;
    }

    public Integer getStoreTipSys() {
        return storeTipSys;
    }

    public void setStoreTipSys(Integer storeTipSys) {
        this.storeTipSys = storeTipSys;
    }

    public Date getValidityTime() {
        return validityTime;
    }

    public void setValidityTime(Date validityTime) {
        this.validityTime = validityTime;
    }

    public String getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(String mainAccount) {
        this.mainAccount = mainAccount;
    }

    @Override
    public String toString() {
        return "DiySetVo{" +
                "token='" + token + '\'' +
                ", deviceNumLimit=" + deviceNumLimit +
                ", validityTime=" + validityTime +
                ", isCall=" + isCall +
                ", talkback=" + talkback +
                ", vedio=" + vedio +
                ", broadcast=" + broadcast +
                ", mapRange=" + mapRange +
                ", trail=" + trail +
                ", listener=" + listener +
                ", vedioTimeLimit=" + vedioTimeLimit +
                ", vedioTimeLimitSys=" + vedioTimeLimitSys +
                ", isAutoUpload=" + isAutoUpload +
                ", isAutoUploadSys=" + isAutoUploadSys +
                ", dispatcherUploadLimit=" + dispatcherUploadLimit +
                ", dispatcherUploadLimitSys=" + dispatcherUploadLimitSys +
                ", appLog=" + appLog +
                ", appLogSys=" + appLogSys +
                ", dispatcherLog=" + dispatcherLog +
                ", dispatcherLogSys=" + dispatcherLogSys +
                ", storeTip=" + storeTip +
                ", storeTipSys=" + storeTipSys +
                ", mainAccount=" + mainAccount +
                '}';
    }
}
