package com.wyfx.business.dao;

import com.wyfx.business.entity.RoleMenu;
import com.wyfx.business.entity.vo.RoleMenuVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMenuMapper {
    int insert(RoleMenu record);

    int insertSelective(RoleMenu record);

    /**
     * 批量更新角色权限
     *
     * @param roleId 角色Id
     * @param menus  权限集合
     */
    void updateUserMenuManager(@Param("roleId") int roleId, @Param("menus") List<Integer> menus);

    /**
     * 根据用户角色查询角色权限
     *
     * @param roleId
     * @return
     */
    List<RoleMenuVo> findUserPermissions(int roleId);

    RoleMenu findPermissionsByRoleIdAndMid(@Param("roleId") Long roleId, @Param("mid") Long mid);

    void delete(@Param("roleId") Long roleId, @Param("menuId") Long menuId);
}