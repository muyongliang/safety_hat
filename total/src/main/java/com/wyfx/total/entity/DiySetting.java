package com.wyfx.total.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//自定义设置
@ApiModel()
public class DiySetting {

    @ApiModelProperty(hidden = true)
    private Long did;
    @ApiModelProperty(value = "是否支持语音通话 0支持，1不支持 ", name = "isCall", required = true)
    private Integer isCall;//是否支持语音通话 0支持，1不支持  以下一样
    @ApiModelProperty(value = "是否支持对讲 0支持，1不支持 ", name = "talkback", required = true)
    private Integer talkback;//是否支持对讲
    @ApiModelProperty(value = "是否支持视频通话 0支持，1不支持 ", name = "vedio", required = true)
    private Integer vedio; //是否支持视频通话
    @ApiModelProperty(value = "是否支持广播 0支持，1不支持 ", name = "broadcast", required = true)
    private Integer broadcast; //是否支持广播
    @ApiModelProperty(value = "是否支持电子围栏 0支持，1不支持 ", name = "mapRange", required = true)
    private Integer mapRange;//是否支持电子围栏
    @ApiModelProperty(value = "是否支持轨迹 0支持，1不支持 ", name = "trail", required = true)
    private Integer trail;//轨迹
    @ApiModelProperty(value = "是否支持无声侦护 0支持，1不支持 ", name = "listener", required = true)
    private Integer listener;//无声侦护

    @ApiModelProperty(hidden = true)
    private String bid;//企业uuID

    @ApiModelProperty(value = "终端视频录制时长限制(单位:分钟)", name = "vedioTimeLimit", required = true)
    private Integer vedioTimeLimit;//终端视频录制时长限制(单位:分钟)
    @ApiModelProperty(value = "终端视频录制时长状态{0:跟随系统配置;1:无限制;2:限制}", name = "vedioTimeLimitSys", required = true)
    private Integer vedioTimeLimitSys;//终端视频录制时长状态{0:跟随系统配置;1:无限制;2:限制}

    @ApiModelProperty(value = "终端视频是否自动上传(单位:M)", name = "isAutoUpload", required = true)
    private Integer isAutoUpload;//终端视频是否自动上传(单位:M)
    @ApiModelProperty(value = "终端视频自动上传状态{0:跟随系统配置;1:无限制;2:限制}", name = "isAutoUploadSys", required = true)
    private Integer isAutoUploadSys;//终端视频自动上传状态{0:跟随系统配置;1:无限制;2:限制}

    @ApiModelProperty(value = "调度员视频自动上传限制(单位:M)", name = "dispatcherUploadLimit", required = true)
    private Integer dispatcherUploadLimit;//调度员视频自动上传限制(单位:M)
    @ApiModelProperty(value = "调度员app视频自动上传状态{0:跟随系统配置;1:无限制;2:限制}", name = "dispatcherUploadLimitSys", required = true)
    private Integer dispatcherUploadLimitSys;//调度员app视频自动上传状态{0:跟随系统配置;1:无限制;2:限制}

    @ApiModelProperty(value = "终端日志记录开关(几天后自动清理日志)", name = "appLog", required = true)
    private Integer appLog;//终端日志记录开关(几天后自动清理日志)   todo 2限制才有值，其他两项都为null(即前端不用传该参数)
    @ApiModelProperty(value = "终端日志记录状态{0:跟随系统配置;1:无限制;2:限制}", name = "appLogSys", required = true)
    private Integer appLogSys;//终端日志记录状态{0:跟随系统配置;1:无限制;2:限制}

    @ApiModelProperty(value = "调度员日志记录开关(几天后自动清理日志)", name = "dispatcherLog", required = true)
    private Integer dispatcherLog;//调度员日志记录开关(几天后自动清理日志)
    @ApiModelProperty(value = "调度员app日志记录状态{0:跟随系统配置;1:无限制;2:限制}", name = "dispatcherLogSys", required = true)
    private Integer dispatcherLogSys;//调度员app日志记录状态{0:跟随系统配置;1:无限制;2:限制}

    @ApiModelProperty(value = "企业存储空间报警设置", name = "storeTip", required = true)
    private Integer storeTip;//企业存储空间报警设置
    @ApiModelProperty(value = "企业存储空间报警配置{0:跟随系统配置;1:自定义;}", name = "storeTipSys", required = true)
    private Integer storeTipSys;//企业存储空间报警配置{0:跟随系统配置;1:自定义;}

    public DiySetting(Long did, Integer isCall, Integer talkback, Integer vedio, Integer broadcast, Integer mapRange, Integer trail, Integer listener, String bid, Integer vedioTimeLimit, Integer vedioTimeLimitSys, Integer isAutoUpload, Integer isAutoUploadSys, Integer dispatcherUploadLimit, Integer dispatcherUploadLimitSys, Integer appLog, Integer appLogSys, Integer dispatcherLog, Integer dispatcherLogSys, Integer storeTip, Integer storeTipSys) {
        this.did = did;
        this.isCall = isCall;
        this.talkback = talkback;
        this.vedio = vedio;
        this.broadcast = broadcast;
        this.mapRange = mapRange;
        this.trail = trail;
        this.listener = listener;
        this.bid = bid;
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

    public DiySetting() {
        super();
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
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

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid == null ? null : bid.trim();
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

    @Override
    public String toString() {
        return "DiySetting{" +
                "did=" + did +
                ", isCall=" + isCall +
                ", talkback=" + talkback +
                ", vedio=" + vedio +
                ", broadcast=" + broadcast +
                ", mapRange=" + mapRange +
                ", trail=" + trail +
                ", listener=" + listener +
                ", bid='" + bid + '\'' +
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
                '}';
    }
}