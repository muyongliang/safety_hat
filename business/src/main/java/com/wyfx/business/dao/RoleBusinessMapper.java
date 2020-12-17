package com.wyfx.business.dao;

import com.wyfx.business.entity.Role;
import com.wyfx.business.entity.RoleBusiness;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleBusinessMapper {
    int insert(RoleBusiness record);

    int insertSelective(RoleBusiness record);

    /**
     * 根据企业查询具体的角色ID
     *
     * @param businessId
     * @param roleId
     * @return
     */
    RoleBusiness selectByRoleIdAndBusinessId(@Param("businessId") Long businessId, @Param("roleId") Long roleId);

    /**
     * 查询企业包含的角色
     *
     * @param businessId
     * @return
     */
    List<RoleBusiness> selectByBusinessId(@Param("businessId") Long businessId);

    /**
     * 删除企业关联的角色
     *
     * @param businessId
     * @param roleId
     */
    void deleteByRoleBusiness(@Param("businessId") Long businessId, @Param("roleId") Long roleId);

    /**
     * 查询企业关联的角色集
     *
     * @param businessId
     * @return
     */
    List<Role> selectRoleSetByBusinessId(@Param("businessId") Long businessId);

    /**
     * 查询企业是否关联了指定的角色
     *
     * @param businessId
     * @param roleId
     * @return
     */
    Role selectRoleSetByBusinessIdAndRole(@Param("businessId") Long businessId, @Param("roleId") Long roleId, @Param("roleName") String roleName);

}
