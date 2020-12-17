package com.wyfx.total.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

//总后台向企业后台发布消息管理
public class PullMessage {
    private Long pid;

    private String title;

    private String content;

    //指定接收前端时间的格式
    @DateTimeFormat(pattern = "yyyy-MM-dd HH")
    //指定返回给前端的时间格式
    @JsonFormat(pattern = "yyyy-MM-dd HH", timezone = "GMT+8")
    private Date ptime;

    private Integer flag;

    //ctime 和utime 是在逻辑中创建的时间无需指定 @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date ctime;

    private String creator;

    private String updatePerson;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date utime;

    private String toUsers;

    public PullMessage(Long pid, String title, String content, Date ptime, Integer flag, Date ctime, String creator, String updatePerson, Date utime) {
        this.pid = pid;
        this.title = title;
        this.content = content;
        this.ptime = ptime;
        this.flag = flag;
        this.ctime = ctime;
        this.creator = creator;
        this.updatePerson = updatePerson;
        this.utime = utime;
    }

    public PullMessage(Long pid, String title, String content, Date ptime, Integer flag, Date ctime, String creator, String updatePerson, Date utime, String toUsers) {
        this.pid = pid;
        this.title = title;
        this.content = content;
        this.ptime = ptime;
        this.flag = flag;
        this.ctime = ctime;
        this.creator = creator;
        this.updatePerson = updatePerson;
        this.utime = utime;
        this.toUsers = toUsers;
    }

    public PullMessage() {
        super();
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getPtime() {
        return ptime;
    }

    public void setPtime(Date ptime) {
        this.ptime = ptime;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Date getCtime() {
        return ctime;
    }

    //@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson == null ? null : updatePerson.trim();
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    public String getToUsers() {
        return toUsers;
    }

    public void setToUsers(String toUsers) {
        this.toUsers = toUsers == null ? null : toUsers.trim();
    }

    @Override
    public String toString() {
        return "PullMessage{" +
                "pid=" + pid +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", ptime=" + ptime +
                ", flag=" + flag +
                ", ctime=" + ctime +
                ", creator='" + creator + '\'' +
                ", updatePerson='" + updatePerson + '\'' +
                ", utime=" + utime +
                ", toUsers='" + toUsers + '\'' +
                '}';
    }
}