package com.wyfx.business.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * 导入导出设备模板excel文件vo类
 * create by wsm
 * 2019-11-21
 */
public class DeviceImportExportVo implements Serializable {

    @Excel(name = "设备编号", orderNum = "0")
    private String number;
    @Excel(name = "设备型号", orderNum = "1")
    private String type;
    @Excel(name = "设备IMEI", orderNum = "2")
    private String imei;

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

    @Override
    public String toString() {
        return "DeviceImportVo{" +
                "number='" + number + '\'' +
                ", type='" + type + '\'' +
                ", imei='" + imei + '\'' +
                '}';
    }
}
