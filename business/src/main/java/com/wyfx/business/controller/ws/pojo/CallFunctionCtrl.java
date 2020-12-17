package com.wyfx.business.controller.ws.pojo;

/**
 * @author johnson liu
 * @date 2019/12/12
 * @description 通话功能控制, 如镭射灯的开启等
 */
public class CallFunctionCtrl {
    private String flashlight;
    private String laserlight;
    private String mic;
    private String realName;
    private String socketId;
    private String username;
    private Integer userType;

    public CallFunctionCtrl() {
    }

    public CallFunctionCtrl(String flashlight, String laserlight, String mic, String realName, String socketId, String username, Integer userType) {
        this.flashlight = flashlight;
        this.laserlight = laserlight;
        this.mic = mic;
        this.realName = realName;
        this.socketId = socketId;
        this.username = username;
        this.userType = userType;
    }

    public String getFlashlight() {
        return flashlight;
    }

    public void setFlashlight(String flashlight) {
        this.flashlight = flashlight;
    }

    public String getLaserlight() {
        return laserlight;
    }

    public void setLaserlight(String laserlight) {
        this.laserlight = laserlight;
    }

    public String getMic() {
        return mic;
    }

    public void setMic(String mic) {
        this.mic = mic;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
