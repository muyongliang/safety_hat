package com.wyfx.business.dao;

import com.wyfx.business.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RoleMapper {

    int deleteByPrimaryKey(Long roleId);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long roleId);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<String> selectRoleNameByIds(Long ids);

    List<Map> selectAllRole();

    List<Map> selectByRoleName(@Param("roleName") String roleName, @Param("businessId") Long businessId);

    /**
     * 查询不包含默认角色的角色集
     *
     * @param roleName
     * @param businessId
     * @return
     */
    List<Map> selectByRoleNameNoDefault(@Param("roleName") String roleName, @Param("businessId") Long businessId);

}