package com.wyfx.business.service.shiro;

import com.wyfx.business.config.UserRealm;
import com.wyfx.business.dao.MenuManagerMapper;
import com.wyfx.business.dao.RoleMenuMapper;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.MenuManager;
import com.wyfx.business.entity.RoleMenu;
import com.wyfx.business.entity.UserRole;
import com.wyfx.business.entity.vo.MenuManagerVo;
import com.wyfx.business.entity.vo.RoleMenuVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author johnson liu
 * @date 2019/11/2
 * @description 权限管理
 */
@Service
@Transactional
public class MenuManagerServiceImpl implements MenuManagerService {

    @Autowired
    private MenuManagerMapper meunManagerMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private UserRealm userRealm;

    /**
     * 添加权限
     *
     * @param menuManagerVo
     */
    @Override
    @Transactional
    public void addMenu(MenuManagerVo menuManagerVo) {
        MenuManager meunManager = new MenuManager();
        BeanUtils.copyProperties(menuManagerVo, meunManager);
        meunManager.setCreateTime(new Date());
        meunManagerMapper.insertSelective(meunManager);
    }

    /**
     * 查询菜单权限
     *
     * @param roleId
     * @return
     */
    @Override
    public List<RoleMenuVo> findUserPermissions(int roleId) {
        List<RoleMenuVo> roleMenuVoList = roleMenuMapper.findUserPermissions(roleId);
        return roleMenuVoList;
    }

    /**
     * 查询菜单权限
     *
     * @param roleList
     * @return
     */
    @Override
    public List<RoleMenuVo> findUserPermissions(List<UserRole> roleList) {
        List<RoleMenuVo> list = new ArrayList<>();
        for (int i = 0; i < roleList.size(); i++) {
            List<RoleMenuVo> roleMenuVoList = roleMenuMapper.findUserPermissions(roleList.get(i).getRoleId().intValue());
            for (int j = 0; j < roleMenuVoList.size(); j++) {
                list.add(roleMenuVoList.get(j));
            }
        }
        return list;
    }

    /**
     * 查询按钮权限
     *
     * @param roleId
     * @param parentId
     * @return
     */
    @Override
    public List<MenuManager> findButton(int roleId, int parentId) {
        return null;
    }

    /**
     * 更新某一角色的权限
     *
     * @param roleId
     * @param menus
     */
    @Override
    public void updateUserMenuManager(int roleId, Integer[] menus) {
        List<Integer> list = Arrays.asList(menus);
        roleMenuMapper.updateUserMenuManager(roleId, list);
        userRealm.clearCache();
    }

    /**
     * 获取用户菜单
     *
     * @param parentId
     * @param bid
     * @return
     */
    @Override
    public List<MenuManager> getMenus(int parentId, Long bid) {
        return meunManagerMapper.selectByParentIdAndBid(parentId, bid, 0);
    }

    /**
     * 获取用户按钮权限
     *
     * @param mid 菜单Id
     * @param bid
     * @return
     */
    @Override
    public List<MenuManager> getButtonMenus(int mid, Long bid) {
        return meunManagerMapper.selectByParentIdAndBid(mid, bid, 1);
    }

    /**
     * 获取角色权限
     *
     * @param mid
     * @param roleId
     * @return
     */
    @Override
    public List<MenuManagerVo> getRolePermissions(int mid, BusinessUser businessUser, Long roleId, int type) {
        Long bid = businessUser.getBid();
        List<MenuManager> menuManagerList = (businessUser.getUserType() == 3) ? meunManagerMapper.selectByParentIdAndBidForSub(mid, bid, type) : meunManagerMapper.selectByParentIdAndBid(mid, bid, type);
        List<RoleMenuVo> roleMenuVoList = roleMenuMapper.findUserPermissions(roleId.intValue());
        List<MenuManagerVo> menuManagerVoList = new ArrayList<>();
        Boolean flag = false;
        if (menuManagerList != null) {
            for (MenuManager menuManager : menuManagerList) {
                MenuManagerVo menuManagerVo = new MenuManagerVo();
                BeanUtils.copyProperties(menuManager, menuManagerVo);
                flag = false;
                for (RoleMenuVo roleMenuVo : roleMenuVoList) {
                    if (menuManager.getMid().equals(roleMenuVo.getMid())) {
                        flag = true;
                        break;
                    }
                }
                menuManagerVo.setIsAllowed(flag);
                menuManagerVoList.add(menuManagerVo);
            }
        } else {
            MenuManagerVo menuManagerVo = new MenuManagerVo();
            menuManagerVoList.add(menuManagerVo);
        }
        return menuManagerVoList;
    }

    @Override
    @Transactional
    public void updatePermissions(Long roleId, MenuManagerVo[] menuManagerVo) {
        for (MenuManagerVo managerVo : menuManagerVo) {
            RoleMenu roleMenu = roleMenuMapper.findPermissionsByRoleIdAndMid(roleId, managerVo.getMid());
            if (managerVo.getIsAllowed() && roleMenu == null) {
                //新增权限
                roleMenu = new RoleMenu(roleId, managerVo.getMid());
                roleMenuMapper.insert(roleMenu);
            }
            if (!managerVo.getIsAllowed() && roleMenu != null) {
                //删除权限
                roleMenuMapper.delete(roleId, managerVo.getMid());
            }
        }
    }
}
