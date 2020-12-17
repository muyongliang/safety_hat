package com.wyfx.business.service.shiro;

import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.MenuManager;
import com.wyfx.business.entity.UserRole;
import com.wyfx.business.entity.vo.MenuManagerVo;
import com.wyfx.business.entity.vo.RoleMenuVo;

import java.util.List;

public interface MenuManagerService {
    /**
     * 添加权限
     *
     * @param menuManagerVo
     */
    void addMenu(MenuManagerVo menuManagerVo);

    /**
     * 查询菜单权限
     *
     * @return
     */
    List<RoleMenuVo> findUserPermissions(int roleId);

    /**
     * 查询菜单权限
     *
     * @return
     */
    List<RoleMenuVo> findUserPermissions(List<UserRole> roleList);

    /**
     * 查询按钮权限
     *
     * @param parentId
     * @return
     */
    List<MenuManager> findButton(int roleId, int parentId);

    /**
     * 更新某一角色的权限
     *
     * @param roleId
     * @param menus
     */
    void updateUserMenuManager(int roleId, Integer[] menus);

    /**
     * 获取用户菜单
     *
     * @param parentId
     * @param bid
     * @return
     */
    List<MenuManager> getMenus(int parentId, Long bid);

    /**
     * 获取用户按钮权限
     *
     * @param mid 菜单Id
     * @param bid
     * @return
     */
    List<MenuManager> getButtonMenus(int mid, Long bid);

    /**
     * 获取角色权限
     *
     * @param mid
     * @param roleId
     * @return
     */
    List<MenuManagerVo> getRolePermissions(int mid, BusinessUser user, Long roleId, int type);

    void updatePermissions(Long roleId, MenuManagerVo[] menuManagerVo);
}
