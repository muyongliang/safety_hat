package com.wyfx.business.entity;

public class AlarmType {
    private Long aid;

    private String type;

    private String tipMessage;

    public AlarmType(Long aid, String type) {
        this.aid = aid;
        this.type = type;
    }

    public AlarmType(Long aid, String type, String tipMessage) {
        this.aid = aid;
        this.type = type;
        this.tipMessage = tipMessage;
    }

    public AlarmType() {
        super();
    }

    public String getTipMessage() {
        return tipMessage;
    }

    public void setTipMessage(String tipMessage) {
        this.tipMessage = tipMessage;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    @Override
    public String toString() {
        return "AlarmType{" +
                "aid=" + aid +
                ", type='" + type + '\'' +
                ", tipMessage='" + tipMessage + '\'' +
                '}';
    }
}