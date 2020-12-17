package com.wyfx.business.dao;

import com.wyfx.business.app.vo.ContactsVo;
import com.wyfx.business.entity.TalkBackGroupMember;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TalkBackGroupMemberMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TalkBackGroupMember record);

    int insertSelective(TalkBackGroupMember record);

    TalkBackGroupMember selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TalkBackGroupMember record);

    int updateByPrimaryKey(TalkBackGroupMember record);

    int deleteByGroupId(Long groupId);

    int deleteByBid(Long bid);

    List<ContactsVo> findByGroupIdAndProjectId(@Param("projectId") Integer projectId, @Param("groupId") Integer groupId);

    List<ContactsVo> findByGroupId(@Param("groupId") Integer groupId);

    List<ContactsVo> findActivateMemberByProjectId(@Param("projectId") Long projectId);

    List<ContactsVo> findByProjectIdFromBusinessUser(@Param("projectId") Integer projectId, @Param("groupId") Integer groupId);

    TalkBackGroupMember selectByBidAndGroupId(@Param("groupId") Long groupId, @Param("bid") Long bid);
}