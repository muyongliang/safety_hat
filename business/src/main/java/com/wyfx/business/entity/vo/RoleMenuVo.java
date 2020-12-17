package com.wyfx.business.entity.vo;

import com.wyfx.business.entity.MenuManager;

import java.util.Date;

/**
 * @author johnson liu
 * @date 2019/11/5
 * @description 角色权限VO类
 */
public class RoleMenuVo extends MenuManager {

    private Long roleId;

    public RoleMenuVo() {

    }

    public RoleMenuVo(Long mid, Long parentId, String name, String url, Integer type, Integer orderNum, Date createTime, Date modifyTime, String remark, Long roleId) {
        super(mid, parentId, name, url, type, orderNum, createTime, modifyTime, remark);
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
