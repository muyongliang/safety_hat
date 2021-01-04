package com.wyfx.business.service;

import com.wyfx.business.app.vo.ContactsVo;
import com.wyfx.business.dao.TalkBackGroupMapper;
import com.wyfx.business.dao.TalkBackGroupMemberMapper;
import com.wyfx.business.entity.TalkBackGroup;
import com.wyfx.business.entity.TalkBackGroupMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author johnson liu
 * @date 2019/11/19
 * @description 对讲组成员业务类
 */
@Service
@Transactional
public class TalkBackGroupMemberServiceImpl implements TalkBackGroupMemberService {

    @Autowired
    private TalkBackGroupMemberMapper talkBackGroupMemberMapper;
    @Autowired
    private TalkBackGroupMapper talkBackGroupMapper;

    /**
     * 根据对讲组的ID查询所有成员信息
     *
     * @param groupId
     */
    @Override
    public List<ContactsVo> findByGroupIdAndProjectId(Integer projectId, Integer groupId) {
        return talkBackGroupMemberMapper.findByGroupIdAndProjectId(projectId, groupId);
    }

    @Override
    public List<ContactsVo> findByGroupId(Integer groupId) {
        return talkBackGroupMemberMapper.findByGroupId(groupId);
    }

    /**
     * 从用户表中查询所有属于该项目但还没在对讲组的用户
     *
     * @param groupId
     * @param projectId
     */
    @Override
    public List<ContactsVo> findByProjectIdFromBusinessUser(Long groupId, Long projectId) {
        return talkBackGroupMemberMapper.findByProjectIdFromBusinessUser(projectId.intValue(), groupId.intValue());
    }

    @Override
    public void moveMemberToProject(Long bid, Long projectId) {
        //查询指定项目的默认对讲组
        TalkBackGroup talkBackGroup = talkBackGroupMapper.selectByNameAndProjectId(projectId, "默认分组");
        talkBackGroupMemberMapper.deleteByBid(bid);
        talkBackGroupMemberMapper.insert(new TalkBackGroupMember(bid, talkBackGroup.getId()));
    }
}
