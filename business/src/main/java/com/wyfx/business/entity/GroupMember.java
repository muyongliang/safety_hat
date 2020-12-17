package com.wyfx.business.entity;

public class GroupMember {
    private Long id;

    private Long gourpId;

    private Long groupMenberId;

    private Integer status;

    public GroupMember(Long id, Long gourpId, Long groupMenberId, Integer status) {
        this.id = id;
        this.gourpId = gourpId;
        this.groupMenberId = groupMenberId;
        this.status = status;
    }

    public GroupMember() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGourpId() {
        return gourpId;
    }

    public void setGourpId(Long gourpId) {
        this.gourpId = gourpId;
    }

    public Long getGroupMenberId() {
        return groupMenberId;
    }

    public void setGroupMenberId(Long groupMenberId) {
        this.groupMenberId = groupMenberId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}