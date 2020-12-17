package com.wyfx.business.controller.ws.pojo;

import java.io.Serializable;

/**
 * @author johnson liu
 * @date 2019/11/21
 * @description 命令基类
 */
public class BaseCommand implements Serializable {
    private String eventName;
    private String type;
    private Object data;
    private Long time;
    private String sessionId;


    public BaseCommand(String eventName, String type, Object data) {
        this.eventName = eventName;
        this.type = type;
        this.data = data;
    }

    public BaseCommand(String eventName, String type, Object data, Long time, String sessionId) {
        this.eventName = eventName;
        this.type = type;
        this.data = data;
        this.time = time;
        this.sessionId = sessionId;
    }

    public BaseCommand() {
    }

    @Override
    public String toString() {
        return "BaseCommand{" +
                "eventName='" + eventName + '\'' +
                ", type='" + type + '\'' +
                ", data=" + data +
                ", time=" + time +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
