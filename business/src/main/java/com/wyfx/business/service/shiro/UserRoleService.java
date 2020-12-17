package com.wyfx.business.service.shiro;

import com.wyfx.business.entity.UserRole;

import java.util.List;

public interface UserRoleService {
    /**
     * 通过用户ID查询用户角色集
     *
     * @param userId
     * @return
     */
    List<UserRole> findByUserId(Integer userId);

    /**
     * 通过用户名查询用户角色集
     *
     * @param userName
     * @return
     */
    List<UserRole> findByUserName(String userName);

    /**
     * 添加用户角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    void addUserRole(Integer userId, Integer roleId);

    /**
     * 更新用户角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    void updateUserRole(Integer userId, Integer roleId);

    /**
     * 批量更新用户角色
     *
     * @param bid
     * @param roles
     */
    void updateUserRoles(Long bid, Object[] roles);

}
