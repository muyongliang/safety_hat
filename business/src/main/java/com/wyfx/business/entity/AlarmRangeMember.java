package com.wyfx.business.entity;

public class AlarmRangeMember {
    private Long mid;

    private Long bid;

    private Long rid;

    private Integer status;

    public AlarmRangeMember(Long mid, Long bid, Long rid, Integer status) {
        this.mid = mid;
        this.bid = bid;
        this.rid = rid;
        this.status = status;
    }

    public AlarmRangeMember() {
        super();
    }

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}