package com.wyfx.business.entity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * 导出在线终端vo类
 * create by wsm on 2019-12-20
 */
public class OnlineClientVo implements Serializable {

    @Excel(name = "姓名", orderNum = "0")
    private String name;
    @Excel(name = "电话", orderNum = "1")
    private String mobile;
    @Excel(name = "工种", orderNum = "2", replace = {"监理_5", "施工_3"})//具体见企业后台字典设置
    private String workType;
    @Excel(name = "今日首次进入时间", orderNum = "3")
    private Date firstIntoTime;
    @Excel(name = "今日最后离开时间", orderNum = "4")
    private Date laseLeaveTime;

    public OnlineClientVo() {
    }


    public OnlineClientVo(String name, String mobile, String workType, Date firstIntoTime, Date laseLeaveTime) {
        this.name = name;
        this.mobile = mobile;
        this.workType = workType;
        this.firstIntoTime = firstIntoTime;
        this.laseLeaveTime = laseLeaveTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public Date getFirstIntoTime() {
        return firstIntoTime;
    }

    public void setFirstIntoTime(Date firstIntoTime) {
        this.firstIntoTime = firstIntoTime;
    }

    public Date getLaseLeaveTime() {
        return laseLeaveTime;
    }

    public void setLaseLeaveTime(Date laseLeaveTime) {
        this.laseLeaveTime = laseLeaveTime;
    }

    @Override
    public String toString() {
        return "OnlineClientVo{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", workType=" + workType +
                ", firstIntoTime=" + firstIntoTime +
                ", laseLeaveTime=" + laseLeaveTime +
                '}';
    }
}
