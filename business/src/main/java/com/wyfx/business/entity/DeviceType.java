package com.wyfx.business.entity;

public class DeviceType {
    private Long id;

    private String type;//T1

    private String uuid;

    private Integer status;//启用禁用启用状态{0:启用;1禁用}

    private Integer dicType;//0暂时代表终端


    public DeviceType(Long id, String type, String uuid, Integer status, Integer dicType) {
        this.id = id;
        this.type = type;
        this.uuid = uuid;
        this.status = status;
        this.dicType = dicType;
    }

    public DeviceType() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDicType() {
        return dicType;
    }

    public void setDicType(Integer dicType) {
        this.dicType = dicType;
    }
}