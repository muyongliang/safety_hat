package com.wyfx.business.entity;

public class RoleBusiness {
    private Long businessId;

    private Long roleId;

    public RoleBusiness(Long businessId, Long roleId) {
        this.businessId = businessId;
        this.roleId = roleId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "RoleBusiness{" +
                "businessId=" + businessId +
                ", roleId=" + roleId +
                '}';
    }
}
