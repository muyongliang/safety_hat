package com.wyfx.total.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

//系统提示信息中心
public class TopMessages {
    private Long mid;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date ctime;

    private String company;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    private Integer type;//消息类型{0:"企业服务到期提醒";1:"第三方企业服务到期提醒";2:"企业存储空间报警提醒"}

    private Integer flag;

    private String store; //企业存储空间预警值

    public TopMessages(Long mid, Date ctime, String company, Date endTime, Integer type, Integer flag, String store) {
        this.mid = mid;
        this.ctime = ctime;
        this.company = company;
        this.endTime = endTime;
        this.type = type;
        this.flag = flag;
        this.store = store;
    }

    public TopMessages() {
        super();
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }


    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "TopMessages{" +
                "mid=" + mid +
                ", ctime=" + ctime +
                ", company='" + company + '\'' +
                ", endTime=" + endTime +
                ", type=" + type +
                ", flag=" + flag +
                '}';
    }
}