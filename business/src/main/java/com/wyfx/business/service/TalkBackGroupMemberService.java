package com.wyfx.business.service;

import com.wyfx.business.app.vo.ContactsVo;

import java.util.List;

public interface TalkBackGroupMemberService {
    /**
     * 根据对讲组的ID查询所有成员信息
     *
     * @param groupId
     */
    List<ContactsVo> findByGroupIdAndProjectId(Integer projectId, Integer groupId);

    List<ContactsVo> findByGroupId(Integer groupId);

    /**
     * 从用户表中查询所有属于该项目但还没在对讲组的用户
     */
    List<ContactsVo> findByProjectIdFromBusinessUser(Long groupId, Long projectId);

    /**
     * 移动成员到指定的项目
     */
    void moveMemberToProject(Long bid, Long projectId);
}
