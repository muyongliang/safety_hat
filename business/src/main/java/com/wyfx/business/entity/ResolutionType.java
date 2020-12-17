package com.wyfx.business.entity;

public class ResolutionType {
    private Long rid;

    private String type;

    public ResolutionType(Long rid, String type) {
        this.rid = rid;
        this.type = type;
    }

    public ResolutionType() {
        super();
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
}