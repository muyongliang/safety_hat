package com.wyfx.business.entity;

public class BusinessAreas {
    private String aid;

    private String name;

    private String nameEn;

    private String code;

    private Integer orderBy;

    private String parentId;

    private String remarks;

    private String tbsname;

    public BusinessAreas(String aid, String name, String nameEn, String code, Integer orderBy, String parentId, String remarks, String tbsname) {
        this.aid = aid;
        this.name = name;
        this.nameEn = nameEn;
        this.code = code;
        this.orderBy = orderBy;
        this.parentId = parentId;
        this.remarks = remarks;
        this.tbsname = tbsname;
    }

    public BusinessAreas() {
        super();
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid == null ? null : aid.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn == null ? null : nameEn.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public String getTbsname() {
        return tbsname;
    }

    public void setTbsname(String tbsname) {
        this.tbsname = tbsname == null ? null : tbsname.trim();
    }
}