package com.wyfx.total.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

//字典
public class Dictionaries {

    private Integer did;

    private Integer type;//0 暂时代表设备型号

    private String optionName;//s1

    private Integer flag;//状态标记{0:已启用;1:禁用}

    private Integer orderNum; //排序

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private String uuid;

    public Dictionaries(Integer did, Integer type, String optionName, Integer flag, Integer orderNum, Date updateTime, String uuid) {
        this.did = did;
        this.type = type;
        this.optionName = optionName;
        this.flag = flag;
        this.orderNum = orderNum;
        this.updateTime = updateTime;
        this.uuid = uuid;
    }

    public Dictionaries() {
    }

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName == null ? null : optionName.trim();
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "Dictionaries{" +
                "did=" + did +
                ", type=" + type +
                ", optionName='" + optionName + '\'' +
                ", flag=" + flag +
                ", orderNum=" + orderNum +
                ", updateTime=" + updateTime +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}