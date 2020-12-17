package com.wyfx.business.config;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author johnson liu
 * @date 2019/11/23
 * @description 自定义验证用户请求来源
 */
public class MyUsernamePasswordToken extends UsernamePasswordToken {

    private Integer source;

    private String imei;

    public MyUsernamePasswordToken() {

    }

    public MyUsernamePasswordToken(String username, String password, Integer source) {
        super(username, password);
        this.source = source;
    }

    /**
     * 免密登录
     * source 666 时免密登录
     *
     * @param username
     */
    public MyUsernamePasswordToken(String username, Integer source) {
        super(username, "");
        this.source = source;
    }

    public MyUsernamePasswordToken(String username, String password, Integer source, String imei) {
        super(username, password);
        this.imei = imei;
        this.source = source;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
