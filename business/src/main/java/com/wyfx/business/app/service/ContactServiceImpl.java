package com.wyfx.business.app.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.app.vo.ContactsForLocationVo;
import com.wyfx.business.app.vo.ContactsVo;
import com.wyfx.business.app.vo.MemberDetailVo;
import com.wyfx.business.dao.BusinessUserMapper;
import com.wyfx.business.dao.GpsMapper;
import com.wyfx.business.dao.GroupInfoMapper;
import com.wyfx.business.dao.GroupMemberMapper;
import com.wyfx.business.entity.GroupInfo;
import com.wyfx.business.entity.GroupMember;
import com.wyfx.business.entity.vo.MemberVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author johnson liu
 * @date 2019/11/15
 * @description 客户端通讯录
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ContactServiceImpl implements ContactService {

    @Autowired
    private BusinessUserMapper businessUserMapper;
    @Autowired
    private GroupInfoMapper groupInfoMapper;
    @Autowired
    private GroupMemberMapper groupMemberMapper;
    @Autowired
    private GpsMapper gpsMapper;

    /**
     * 分页查询通讯录中的调度员和终端账号
     *
     * @param projectId
     * @return
     */
    @Override
    public PageInfo<ContactsVo> findContactByProjectId(Long bid, Integer projectId, Integer pageSize, Integer pageNum) {
        if (pageSize != null && pageNum != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<ContactsForLocationVo> list = businessUserMapper.findContactAndLatLngByProjectId(projectId, bid);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据姓名查询
     *
     * @return
     */
    @Override
    public PageInfo<ContactsVo> findContactByName(Integer projectId, String name, Integer pageSize, Integer pageNum) {
        if (pageSize != null && pageNum != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<ContactsVo> list = businessUserMapper.findContactByName(projectId, name);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据工种查询
     *
     * @return
     */
    @Override
    public PageInfo<ContactsVo> findContactByWorkType(Integer projectId, Integer workTye, Integer pageSize, Integer pageNum) {
        if (pageSize != null && pageNum != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<ContactsVo> list = businessUserMapper.findContactByWorkType(projectId, workTye);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public PageInfo<ContactsVo> findContactGroupMember(Long groupId, Integer pageSize, Integer pageNum) {
        if (pageSize != null && pageNum != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<ContactsVo> list = groupMemberMapper.selectByGroupIdAndStatus(groupId, 1);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据终端ID查询终端详细信息
     *
     * @param clientId
     * @return
     */
    @Override
    public Object findByClientId(Integer clientId) {
        return null;
    }

    /**
     * 获取企业成员语音等功能的权限
     *
     * @return
     */
    @Override
    public Object getFunction() {
        return null;
    }

    /**
     * 添加通讯录群组
     */
    @Override
    @Transactional
    public void addContactGroup(Integer projectId, MemberVo memberVo) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupName(memberVo.getGroupName());
        groupInfo.setProjectId(projectId.longValue());
        groupInfoMapper.insert(groupInfo);
        List<GroupMember> memberList = new ArrayList<>();
        List<ContactsVo> addList = memberVo.getAdd();
        List<ContactsVo> waitAddList = memberVo.getWaitAdd();
        for (int i = 0; i < addList.size(); i++) {
            GroupMember member = new GroupMember(null, groupInfo.getGroupId(), addList.get(i).getId().longValue(), 1);
            memberList.add(member);
        }
        for (int i = 0; i < waitAddList.size(); i++) {
            GroupMember member = new GroupMember(null, groupInfo.getGroupId(), waitAddList.get(i).getId().longValue(), 0);
            memberList.add(member);
        }
        groupMemberMapper.insertForeach(memberList);
    }

    /**
     * 更新通讯录群组
     */
    @Override
    @Transactional
    public void updateContactGroup(Long projectId, MemberVo memberVo) {
        if (memberVo.getGroupName() != null && memberVo.getGroupId() != null) {
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setGroupId(memberVo.getGroupId());
            groupInfo.setGroupName(memberVo.getGroupName());
            groupInfo.setProjectId(projectId);
            groupInfoMapper.updateByPrimaryKey(groupInfo);
        }
        //更新群组成员
        groupMemberMapper.updateForeach(memberVo.getGroupId(), 0, memberVo.getWaitAdd());
        groupMemberMapper.updateForeach(memberVo.getGroupId(), 1, memberVo.getAdd());
    }

    /**
     * 删除群组
     *
     * @param groupId
     */
    @Override
    @Transactional
    public void deleteContactGroup(Integer groupId) {
        groupMemberMapper.deleteByGroupId(groupId.longValue());
        groupInfoMapper.deleteByPrimaryKey(groupId.longValue());
    }

    /**
     * 获取通讯录成员列表
     *
     * @param groupId
     * @return
     */
    @Override
    public MemberVo getMemberList(Long groupId, Long projectId, Long bid) throws Exception {
        List<MemberVo> list = new ArrayList<>();
        MemberVo memberVo = new MemberVo();
        if (groupId != null) {
            GroupInfo groupInfo = groupInfoMapper.selectByPrimaryKey(groupId);
            if (groupInfo == null) {
                throw new Exception("对讲组不存在");
            }
            memberVo.setGroupId(groupId);
            memberVo.setGroupName(groupInfo.getGroupName());
            /*memberVo.setWaitAdd(businessUserMapper.findContactByProjectId(projectId.intValue()));*/
            memberVo.setWaitAdd(groupMemberMapper.selectByGroupIdAndStatus(groupId, 0));
            memberVo.setAdd(groupMemberMapper.selectByGroupIdAndStatus(groupId, 1));
        } else {
            /*memberVo.setWaitAdd(groupMemberMapper.selectByGroupIdAndStatus(groupId,0));
            memberVo.setAdd(groupMemberMapper.selectByGroupIdAndStatus(groupId,1));*/
            memberVo.setAdd(new ArrayList<ContactsVo>());
            memberVo.setWaitAdd(businessUserMapper.findContactByProjectId(projectId.intValue(), bid));
        }
        /*list.add(memberVo);*/
        return memberVo;
    }

    @Override
    public List<GroupInfo> findByGroupName(Long projectId, String groupName) {
        List<GroupInfo> list = (groupName == null || groupName.equals("")) ? groupInfoMapper.selectByProjectId(projectId) : groupInfoMapper.selectByProjectIdAndName(projectId, groupName);
        return list;
    }

    /**
     * 查询成员定位信息
     *
     * @param userId
     * @param type
     * @return
     */
    @Override
    public MemberDetailVo findMemberInfo(Long userId, Integer type) {
        MemberDetailVo memberDetailVo = null;
        switch (type) {
            case 1:
                memberDetailVo = gpsMapper.selectDispatcherInfoByUserId(userId);
                break;
            case 2:
                memberDetailVo = gpsMapper.selectClientInfoByUserId(userId);
                break;
        }
        if (memberDetailVo == null) {
            memberDetailVo = new MemberDetailVo();
        }
        return memberDetailVo;
    }
}
