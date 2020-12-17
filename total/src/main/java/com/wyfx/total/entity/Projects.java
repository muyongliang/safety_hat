package com.wyfx.total.entity;

public class Projects {
    private Long pid;

    private String projectName;

    private String projectAddress;

    private Integer status;

    private String businessId;

    public Projects(Long pid, String projectName, String projectAddress, Integer status, String businessId) {
        this.pid = pid;
        this.projectName = projectName;
        this.projectAddress = projectAddress;
        this.status = status;
        this.businessId = businessId;
    }

    public Projects() {
        super();
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress == null ? null : projectAddress.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId == null ? null : businessId.trim();
    }

    @Override
    public String toString() {
        return "Projects{" +
                "pid=" + pid +
                ", projectName='" + projectName + '\'' +
                ", projectAddress='" + projectAddress + '\'' +
                ", status=" + status +
                ", businessId='" + businessId + '\'' +
                '}';
    }
}