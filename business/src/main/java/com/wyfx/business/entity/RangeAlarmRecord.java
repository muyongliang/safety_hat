package com.wyfx.business.entity;

import java.util.Date;

public class RangeAlarmRecord {
    private Long recordId;

    private Date firstIntoTime;

    private Date lastLeaveTime;

    private Long rid;

    private Long alarmRangeMid;

    private String date;

    private Integer fenceId;

    public RangeAlarmRecord(Long recordId, Date firstIntoTime, Date lastLeaveTime, Long rid, Long alarmRangeMid, String date, Integer fenceId) {
        this.recordId = recordId;
        this.firstIntoTime = firstIntoTime;
        this.lastLeaveTime = lastLeaveTime;
        this.rid = rid;
        this.alarmRangeMid = alarmRangeMid;
        this.date = date;
        this.fenceId = fenceId;
    }

    /*public RangeAlarmRecord(Long recordId, Date firstIntoTime, Date lastLeaveTime, Long rid, Long alarmRangeMid, String date) {
        this.recordId = recordId;
        this.firstIntoTime = firstIntoTime;
        this.lastLeaveTime = lastLeaveTime;
        this.rid = rid;
        this.alarmRangeMid = alarmRangeMid;
        this.date = date;
    }*/

    public RangeAlarmRecord() {
        super();
    }

    public Integer getFenceId() {
        return fenceId;
    }

    public void setFenceId(Integer fenceId) {
        this.fenceId = fenceId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Date getFirstIntoTime() {
        return firstIntoTime;
    }

    public void setFirstIntoTime(Date firstIntoTime) {
        this.firstIntoTime = firstIntoTime;
    }

    public Date getLastLeaveTime() {
        return lastLeaveTime;
    }

    public void setLastLeaveTime(Date lastLeaveTime) {
        this.lastLeaveTime = lastLeaveTime;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Long getAlarmRangeMid() {
        return alarmRangeMid;
    }

    public void setAlarmRangeMid(Long alarmRangeMid) {
        this.alarmRangeMid = alarmRangeMid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date == null ? null : date.trim();
    }
}