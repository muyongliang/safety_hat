package com.wyfx.business.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * @author johnson liu
 * @date 2019/11/7
 * @description 设备信息vo类
 * <p>
 * update by wsm
 * @date 2019-11-21
 */
public class DeviceInfoVo implements Serializable {

    private Long did;

    private Long tid;

    @Excel(name = "设备编号", orderNum = "0")
    private String number;
    @Excel(name = "设备型号", orderNum = "1")
    private String type;
    @Excel(name = "设备IMEI", orderNum = "2")
    private String imei;

    /**
     * 终端账号
     */
    @Excel(name = "使用终端账号", orderNum = "3")
    private String account;
    /**
     * 终端使用人姓名
     */
    @Excel(name = "终端使用人姓名", orderNum = "4")
    private String name;


    @Excel(name = "录入时间", orderNum = "5", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd HH:mm:ss")
    private Date recordTime;

    /**
     * 设备状态{0:空闲中;1:使用中}
     */
    @Excel(name = "状态状态", replace = {"使用中_1", "空闲中_0"}, orderNum = "6")
    private Integer status;


    public DeviceInfoVo() {
    }

    public DeviceInfoVo(Long did, String number, String type, String imei, Integer status, Date recordTime, String account, String name) {
        this.did = did;
        this.number = number;
        this.type = type;
        this.imei = imei;
        this.status = status;
        this.recordTime = recordTime;
        this.account = account;
        this.name = name;
    }

    public DeviceInfoVo(Long did, Long tid, String number, String type, String imei, String account, String name, Date recordTime, Integer status) {
        this.did = did;
        this.tid = tid;
        this.number = number;
        this.type = type;
        this.imei = imei;
        this.account = account;
        this.name = name;
        this.recordTime = recordTime;
        this.status = status;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
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

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    @Override
    public String toString() {
        return "DeviceInfoVo{" +
                "did=" + did +
                ", number='" + number + '\'' +
                ", type='" + type + '\'' +
                ", imei='" + imei + '\'' +
                ", status=" + status +
                ", recordTime=" + recordTime +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
