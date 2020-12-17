package com.wyfx.business.entity;

public class AlarmSetting {
    private Long alarmId;

    private Integer dropSwitch;

    private Integer alarmLeaveSwitch;

    private String alarmLeaveTime;

    private Integer hignWorkAlarm;

    private Long projectId;

    /*@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")*/
    private String beginTime;

    /*@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")*/
    private String endTime;

    public AlarmSetting(Long alarmId, Integer dropSwitch, Integer alarmLeaveSwitch, String alarmLeaveTime, Integer hignWorkAlarm, Long projectId, String beginTime, String endTime) {
        this.alarmId = alarmId;
        this.dropSwitch = dropSwitch;
        this.alarmLeaveSwitch = alarmLeaveSwitch;
        this.alarmLeaveTime = alarmLeaveTime;
        this.hignWorkAlarm = hignWorkAlarm;
        this.projectId = projectId;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public AlarmSetting() {
        super();
    }

    public Long getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Long alarmId) {
        this.alarmId = alarmId;
    }

    public Integer getDropSwitch() {
        return dropSwitch;
    }

    public void setDropSwitch(Integer dropSwitch) {
        this.dropSwitch = dropSwitch;
    }

    public Integer getAlarmLeaveSwitch() {
        return alarmLeaveSwitch;
    }

    public void setAlarmLeaveSwitch(Integer alarmLeaveSwitch) {
        this.alarmLeaveSwitch = alarmLeaveSwitch;
    }

    public String getAlarmLeaveTime() {
        return alarmLeaveTime;
    }

    public void setAlarmLeaveTime(String alarmLeaveTime) {
        this.alarmLeaveTime = alarmLeaveTime == null ? null : alarmLeaveTime.trim();
    }

    public Integer getHignWorkAlarm() {
        return hignWorkAlarm;
    }

    public void setHignWorkAlarm(Integer hignWorkAlarm) {
        this.hignWorkAlarm = hignWorkAlarm;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}