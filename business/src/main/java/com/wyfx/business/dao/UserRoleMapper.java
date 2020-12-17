package com.wyfx.business.dao;

import com.wyfx.business.entity.UserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleMapper {
    int insert(UserRole record);

    int insertSelective(UserRole record);

    /**
     * 根据用户ID查询用户角色集
     *
     * @param userId
     * @return
     */
    List<UserRole> findByUserId(int userId);

    /**
     * 更新用户角色
     *
     * @param record
     * @return
     */
    int updateUserRole(UserRole record);

    /**
     * 删除角色
     *
     * @param user_id
     * @return
     */
    int deleteByPrimaryKey(Long user_id);

    /**
     * 根据角色删除
     *
     * @param roleId
     * @return
     */
    int deleteByRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);

    int deleteByUserId(@Param("userId") Long userId);
}