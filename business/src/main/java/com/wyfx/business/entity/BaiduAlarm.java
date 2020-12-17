package com.wyfx.business.entity;

public class BaiduAlarm {
    private Long id;

    private Integer serviceId;

    private Integer fenceId;

    private String fenceName;

    private String monitoredPerson;

    private String action;

    private Long alarmPoint;

    private Long prePoint;

    public BaiduAlarm(Long id, Integer serviceId, Integer fenceId, String fenceName, String monitoredPerson, String action, Long alarmPoint, Long prePoint) {
        this.id = id;
        this.serviceId = serviceId;
        this.fenceId = fenceId;
        this.fenceName = fenceName;
        this.monitoredPerson = monitoredPerson;
        this.action = action;
        this.alarmPoint = alarmPoint;
        this.prePoint = prePoint;
    }

    public BaiduAlarm() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getFenceId() {
        return fenceId;
    }

    public void setFenceId(Integer fenceId) {
        this.fenceId = fenceId;
    }

    public String getFenceName() {
        return fenceName;
    }

    public void setFenceName(String fenceName) {
        this.fenceName = fenceName == null ? null : fenceName.trim();
    }

    public String getMonitoredPerson() {
        return monitoredPerson;
    }

    public void setMonitoredPerson(String monitoredPerson) {
        this.monitoredPerson = monitoredPerson == null ? null : monitoredPerson.trim();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action == null ? null : action.trim();
    }

    public Long getAlarmPoint() {
        return alarmPoint;
    }

    public void setAlarmPoint(Long alarmPoint) {
        this.alarmPoint = alarmPoint;
    }

    public Long getPrePoint() {
        return prePoint;
    }

    public void setPrePoint(Long prePoint) {
        this.prePoint = prePoint;
    }
}