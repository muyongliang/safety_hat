package com.wyfx.business.entity.vo;

/**
 * @author johnson liu
 * @date 2019/11/9
 * @description 终端账号查询Vo(早前使用)
 */
public class ClientAccountFindVo {

    private Long clientId;

    private String account;

    private String name;

    private String tel;
    /**
     * 启用状态
     */
    private Integer status;

    private String workType;

    private String number;

    private Integer online_status;

    private Integer diyFlag;//是否采用系统设置{0:系统默认;1:自定义}
    private String resolution;//视频分辨率

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getOnline_status() {
        return online_status;
    }

    public void setOnline_status(Integer online_status) {
        this.online_status = online_status;
    }

    public Integer getDiyFlag() {
        return diyFlag;
    }

    public void setDiyFlag(Integer diyFlag) {
        this.diyFlag = diyFlag;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
