package com.wyfx.business.entity;

public class RangeClient {
    private Long rcId;

    private Long alarmRangeId;

    private Long clientId;

    public RangeClient(Long rcId, Long alarmRangeId, Long clientId) {
        this.rcId = rcId;
        this.alarmRangeId = alarmRangeId;
        this.clientId = clientId;
    }

    public RangeClient() {
        super();
    }

    public Long getRcId() {
        return rcId;
    }

    public void setRcId(Long rcId) {
        this.rcId = rcId;
    }

    public Long getAlarmRangeId() {
        return alarmRangeId;
    }

    public void setAlarmRangeId(Long alarmRangeId) {
        this.alarmRangeId = alarmRangeId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}