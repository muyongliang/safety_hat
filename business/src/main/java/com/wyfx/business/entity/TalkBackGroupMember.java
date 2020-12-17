package com.wyfx.business.entity;

public class TalkBackGroupMember {
    private Long id;

    private Long businessId;

    private Long groupId;

    public TalkBackGroupMember(Long id, Long businessId, Long groupId) {
        this.id = id;
        this.businessId = businessId;
        this.groupId = groupId;
    }

    public TalkBackGroupMember(Long businessId, Long groupId) {
        this.businessId = businessId;
        this.groupId = groupId;
    }

    public TalkBackGroupMember() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}