package com.wyfx.business.service.shiro;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.config.UserRealm;
import com.wyfx.business.controller.commons.SafetyHatException;
import com.wyfx.business.dao.*;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.Role;
import com.wyfx.business.entity.RoleBusiness;
import com.wyfx.business.entity.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author johnson liu
 * @date 2019/11/2
 * @description 角色管理
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService, UserRoleService {
    private final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleBusinessMapper roleBusinessMapper;
    @Autowired
    private BusinessUserMapper businessUserMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private UserRealm userRealm;

    @Override
    @Transactional
    public void addRole(Role role) {
        Role roleFromRoleBusiness = roleBusinessMapper.selectRoleSetByBusinessIdAndRole(role.getBusinessId(), null, role.getRoleName());
        if (roleFromRoleBusiness != null) {
            throw new SafetyHatException("该角色已经存在");
        }
        Role addRole = new Role();
        List<Map> list = roleMapper.selectByRoleName(role.getRoleName(), role.getBusinessId());
        if (list == null || list.size() < 1) {//角色表中没有该角色
            BeanUtils.copyProperties(role, addRole);
            addRole.setBusinessId(0L);
            roleMapper.insert(addRole);//添加角色
            roleBusinessMapper.insert(new RoleBusiness(role.getBusinessId(), addRole.getRoleId()));//角色和企业关联
        }
    }

    /**
     * 编辑角色
     *
     * @param role
     */
    @Override
    @Transactional
    public void editRole(Role role) {
        roleMapper.updateByPrimaryKeySelective(role);
        userRealm.clearCache();
    }

    /**
     * 通过用户ID查询用户角色集
     *
     * @param userId
     * @return
     */
    @Override
    public List<UserRole> findByUserId(Integer userId) {
        return userRoleMapper.findByUserId(userId);
    }

    /**
     * 通过用户名查询用户角色集
     *
     * @param userName
     * @return
     */
    @Override
    public List<UserRole> findByUserName(String userName) {
        BusinessUser businessUser = businessUserMapper.findByUserName(userName);
        int userId = businessUser.getBid().intValue();
        return userRoleMapper.findByUserId(userId);
    }

    /**
     * 添加用户角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    @Override
    public void addUserRole(Integer userId, Integer roleId) {
        UserRole userRole = new UserRole(userId.longValue(), roleId.longValue());
        userRoleMapper.insert(userRole);
    }

    /**
     * 更新用户角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    @Override
    public void updateUserRole(Integer userId, Integer roleId) {
        UserRole userRole = new UserRole(userId.longValue(), roleId.longValue());
        userRoleMapper.updateUserRole(userRole);
        userRealm.clearCache();
    }

    @Override
    public void updateUserRoles(Long bid, Object[] roles) {
        logger.info("角色列表====" + Arrays.toString(roles));
        userRoleMapper.deleteByUserId(bid);
        UserRole userRole = null;
        //绑定角色
        for (int i = 0; i < roles.length; i++) {
            userRole = new UserRole(bid, ((Integer) roles[i]).longValue());
            userRoleMapper.insert(userRole);
        }
    }

    /**
     * 根据ID查询角色详情
     *
     * @param roleId
     * @return
     */
    @Override
    public Role findDetailById(Long roleId) {
        Role role = roleMapper.selectByPrimaryKey(roleId);
        return role;
    }

    /**
     * 分页查询所有角色信息
     *
     * @param businessID
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo findAllRoles(String businessID, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        /*List<ServerListVo> list= serverInfoMapper.selectByIsDelete(isDelete);*/
        /*PageInfo pageInfo=new PageInfo(list);*/
        return null;
    }

    /**
     * 根据角色名称查询角色信息
     *
     * @param roleName
     * @return
     */
    @Override
    public PageInfo<Role> findDetailByRoleName(Long businessId, String roleName, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        /*List<Map> list= roleMapper.selectByRoleNameNoDefault(roleName,businessId);*/
        List<Map> list = roleMapper.selectByRoleName(roleName, businessId);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }


    /**
     * 分配子管理员时 获取所有角色列表
     *
     * @return
     */
    @Override
    public List<Map> selectAllRole(Long businessId) {
        /*List<Map> mapList = roleMapper.selectAllRole();*/
        List<Map> mapList = roleMapper.selectByRoleNameNoDefault(null, businessId);
        return mapList;
    }

    @Override
    @Transactional
    public void deleteRole(BusinessUser businessUser, Long roleId) {
        List<Map> list = roleMapper.selectByRoleName(null, businessUser.getBusinessId());
        int num = 0;
        for (Map map : list) {
            if (map.get("role_id").equals(roleId)) {
                num = Integer.valueOf(map.get("num").toString());
            }
        }
        if (num == 0) {
            roleBusinessMapper.deleteByRoleBusiness(businessUser.getBusinessId(), roleId);//删除企业关联的角色
            userRoleMapper.deleteByRoleId(businessUser.getBid(), roleId);//删除用户关联的角色
            roleMenuMapper.delete(roleId, null);//删除角色关联的权限
            roleMapper.deleteByPrimaryKey(roleId);//删除角色
        } else {
            throw new SafetyHatException("不能删除已绑定的角色");
        }
    }

    @Override
    public boolean checkRoleName(Long businessId, String roleName) {
        List<Map> list = roleMapper.selectByRoleName(roleName, businessId);
        return list == null || list.size() <= 0;
    }
}
