package com.wyfx.business.entity;

public class GroupInfo {
    private Long groupId;

    private String groupName;

    private Long projectId;

    public GroupInfo(Long groupId, String groupName, Long projectId) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.projectId = projectId;
    }

    public GroupInfo() {
        super();
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = (projectId == null) ? 0L : projectId;
    }
}