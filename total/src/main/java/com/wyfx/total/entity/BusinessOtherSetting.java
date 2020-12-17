package com.wyfx.total.entity;


public class BusinessOtherSetting {
    private Long otherId;

    private Long sid;

    private Long bid;

    private Integer type;

    private Double size;

    public BusinessOtherSetting(Long otherId, Long sid, Long bid, Integer type, Double size) {
        this.otherId = otherId;
        this.sid = sid;
        this.bid = bid;
        this.type = type;
        this.size = size;
    }

    public BusinessOtherSetting() {
        super();
    }

    public Long getOtherId() {
        return otherId;
    }

    public void setOtherId(Long otherId) {
        this.otherId = otherId;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }
}