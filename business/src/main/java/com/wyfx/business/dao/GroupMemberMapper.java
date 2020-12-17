package com.wyfx.business.dao;

import com.wyfx.business.app.vo.ContactsVo;
import com.wyfx.business.entity.GroupMember;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GroupMember record);

    int insertSelective(GroupMember record);

    GroupMember selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GroupMember record);

    int updateByPrimaryKey(GroupMember record);

    /**
     * 批量插入数据
     *
     * @param memberList
     * @return
     */
    int insertForeach(@Param("memberList") List<GroupMember> memberList);

    int deleteByGroupId(Long groupId);

    /**
     * 查询群组成员列表
     *
     * @param groupId
     * @param status
     * @return
     */
    List<ContactsVo> selectByGroupIdAndStatus(@Param("groupId") Long groupId, @Param("status") Integer status);

    /**
     * 批量更新通讯录成员状态
     *
     * @return
     */
    int updateForeach(@Param("groupId") Long groupId, @Param("status") Integer status, @Param("members") List<ContactsVo> members);
}