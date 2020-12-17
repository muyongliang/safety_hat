package com.wyfx.business.entity.vo;

import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public class MessageVo extends Object {
    private Long createTime;
    private String des;
    private List<T> member;
    private Object my;
    private Object room;
    private List<MemberBeanVo> target;

    public MessageVo(Long createTime, String des, List<T> member, Object my, Object room, List<MemberBeanVo> target) {
        this.createTime = createTime;
        this.des = des;
        this.member = member;
        this.my = my;
        this.room = room;
        this.target = target;
    }

    public MessageVo() {
    }

    @Override
    public String toString() {
        return "MessageVo{" +
                "createTime=" + createTime +
                ", des='" + des + '\'' +
                ", member=" + member +
                ", my=" + my +
                ", room=" + room +
                ", target=" + target +
                '}';
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public List<T> getMember() {
        return member;
    }

    public void setMember(List<T> member) {
        this.member = member;
    }

    public Object getMy() {
        return my;
    }

    public void setMy(Object my) {
        this.my = my;
    }

    public Object getRoom() {
        return room;
    }

    public void setRoom(Object room) {
        this.room = room;
    }

    public List<MemberBeanVo> getTarget() {
        return target;
    }

    public void setTarget(List<MemberBeanVo> target) {
        this.target = target;
    }
}
