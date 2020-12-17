package com.wyfx.business.app.vo;

/**
 * @author johnson liu
 * @date 2019/12/26
 * @description 功能信息
 */
public class FunctionVo {
    private Integer isCall = 0;//是否支持语音通话 0支持，1不支持

    private Integer talkback = 0;//是否支持对讲  0支持，1不支持

    private Integer vedio = 0; //是否支持视频通话  0支持，1不支持

    private Integer broadcast = 0; //是否支持广播  0支持，1不支持

    private Integer mapRange = 0;//是否支持电子围栏  0支持，1不支持

    private Integer trail = 0;//轨迹  0支持，1不支持

    private Integer listener = 0;//无声侦护  0支持，1不支持

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
}
