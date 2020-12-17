package com.wyfx.business.service.shiro;

import com.github.pagehelper.PageInfo;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {
    /**
     * 添加角色
     *
     * @param role
     */
    void addRole(Role role);

    /**
     * 编辑角色
     *
     * @param role
     */
    void editRole(Role role);

    void deleteRole(BusinessUser businessUser, Long roleId);

    boolean checkRoleName(Long businessId, String roleName);

    /**
     * 根据ID查询角色详情
     *
     * @param roleId
     * @return
     */
    Role findDetailById(Long roleId);

    /**
     * 分页查询所有角色信息
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo findAllRoles(String businessID, int pageNum, int pageSize);

    /**
     * 根据角色名称查询角色信息
     *
     * @param roleName
     * @return
     */
    PageInfo<Role> findDetailByRoleName(Long businessId, String roleName, Integer pageNum, Integer pageSize);
    /*PageInfo<Role> findDetailByRoleName(String roleName,Integer pageNum,Integer pageSize);*/

    /**
     * create by wsm on 2019-12-13
     * 子管理员分配角色使用
     *
     * @return
     */
    List<Map> selectAllRole(Long businessId);

}
