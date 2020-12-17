package com.wyfx.business.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.app.vo.ContactsVo;
import com.wyfx.business.controller.commons.SafetyHatException;
import com.wyfx.business.controller.ws.pojo.BaseCommand;
import com.wyfx.business.controller.ws.pojo.WsConstant;
import com.wyfx.business.dao.BusinessUserMapper;
import com.wyfx.business.dao.ClientAccountMapper;
import com.wyfx.business.dao.TalkBackGroupMapper;
import com.wyfx.business.dao.TalkBackGroupMemberMapper;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.TalkBackGroup;
import com.wyfx.business.entity.TalkBackGroupMember;
import com.wyfx.business.entity.vo.TalkBackGroupVo;
import com.wyfx.business.utils.ConstantList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author johnson liu
 * @date 2019/11/18
 * @description 对讲组业务类
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TalkBackServiceImpl implements TalkBackService {

    private static final Logger logger = LoggerFactory.getLogger(TalkBackServiceImpl.class);

    @Autowired
    private BusinessUserMapper businessUserMapper;

    @Autowired
    private TalkBackGroupMapper talkBackGroupMapper;

    @Autowired
    private TalkBackGroupMemberMapper talkBackGroupMemberMapper;

    @Autowired
    private ClientAccountMapper clientAccountMapper;

    @Override
    public void insertMember(Long bid, Long groupId) {
        TalkBackGroupMember talkBackGroupMember = new TalkBackGroupMember(bid, groupId);
        talkBackGroupMemberMapper.insert(talkBackGroupMember);
    }

    /**
     * 查询默认的对讲组
     *
     * @param projectId
     * @return
     */
    @Override
    public TalkBackGroup findDefaultByProjectId(Long projectId) {
        return talkBackGroupMapper.selectByNameAndProjectId(projectId, "默认分组");
    }

    @Override
    public TalkBackGroup findByGroupId(Long groupId) {
        return talkBackGroupMapper.selectByPrimaryKey(groupId);
    }

    /**
     * 查询终端用户当前所在对讲组
     *
     * @param businessUser
     * @return
     */
    @Override
    public TalkBackGroup findClientTalkBack(BusinessUser businessUser) {
        return talkBackGroupMapper.selectActivateByProjectId(businessUser.getProjectId());
    }

    /**
     * 添加或编辑对讲组
     *
     * @param id
     */
    @Override
    @Transactional
    public void updateTalkBack(Long id, TalkBackGroup talkBackGroup, BusinessUser user, boolean fromApp) throws SafetyHatException {
        //检测是否存在该名称的对讲组
        TalkBackGroup group = talkBackGroupMapper.selectByNameAndProjectId(talkBackGroup.getProjectId(), talkBackGroup.getName());
        if (group != null && group.getIsLock() != -1) {
            throw new SafetyHatException("该项目下已存在此名称的对讲组");
        }
        if (group != null && group.getIsLock() == -1) {
            talkBackGroup.setIsLock(0);
        }
        if (id == null && group == null) {
            talkBackGroupMapper.insertSelective(talkBackGroup);
            if (!fromApp) {
                updateMember(talkBackGroup.getId(), user.getBusinessId(), new Object[0], user.getProjectId());
            }
            //将调度员添加到对讲组成员列表中
            /*TalkBackGroupMember member=new TalkBackGroupMember(talkBackGroup.getBid(),talkBackGroup.getId());
            talkBackGroupMemberMapper.insert(member);*/
            return;
        }
        talkBackGroupMapper.updateByPrimaryKeySelective(talkBackGroup);
    }

    /**
     * 更新对讲组成员
     *
     * @param groupId
     * @param memberIds
     */
    @Override
    @Transactional
    public void updateMember(Long groupId, Long businessId, Object[] memberIds, Long projectId) {
        //先删除该分组之前的所有组成员，然后再进行添加
        talkBackGroupMemberMapper.deleteByGroupId(groupId);
        List list = businessUserMapper.findDispatcherByProjectId(projectId);
        /*通过set去重*/
        HashSet set = new HashSet();
        int size = (memberIds.length >= list.size()) ? memberIds.length : list.size();

        for (int i = 0; i < size; i++) {
            if (i < list.size()) {
                set.add(list.get(i));
            }
            if (i < memberIds.length) {
                set.add(Long.valueOf(String.valueOf(memberIds[i])));
            }
        }

        TalkBackGroupMember member = null;
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            member = new TalkBackGroupMember(Long.valueOf(String.valueOf(iterator.next())), groupId);
            talkBackGroupMemberMapper.insert(member);
        }
    }

    /**
     * 切换对讲组激活状态
     *
     * @param id
     */
    @Override
    @Transactional
    public void switchActivateStatus(Long id, Integer projectId, BusinessUser user) {
        List<ContactsVo> listOfBefore = talkBackGroupMemberMapper.findActivateMemberByProjectId(projectId.longValue());
        //先改变原激活对讲组的激活状态
        talkBackGroupMapper.updateByActivate(projectId.longValue(), TalkBackGroup.NOT_ACTIVATE);
        talkBackGroupMapper.updateByActivateAndId(id, TalkBackGroup.ACTIVATE);
        //通知客户端改变了激活对讲组
        TalkBackGroup talkBackGroup = talkBackGroupMapper.selectActivateByProjectId(projectId.longValue());
        //查询对讲组所有成员
        List<ContactsVo> list = talkBackGroupMemberMapper.findByGroupId(id.intValue());

        TalkBackGroupMember talkBackGroupMember = null;
        boolean isInGrouping = true;
        Map<String, Object> map = new HashMap();
        Map<String, ContactsVo> sendMap = new HashMap<>();
        try {
            for (ContactsVo contactsVo : listOfBefore) {
                isInGrouping = true;
                if (contactsVo.getType() == 2) {
                    talkBackGroupMember = talkBackGroupMemberMapper.selectByBidAndGroupId(id, contactsVo.getId().longValue());//查询终端是否在激活对讲组
                    if (talkBackGroupMember == null) {
                        isInGrouping = false;
                    }
                }
                map.put("groupName", talkBackGroup.getName());
                map.put("groupId", talkBackGroup.getId());
                map.put("isInGrouping", isInGrouping);
                ConstantList.sendMessage(contactsVo.getUserName(), JSON.toJSONString(new BaseCommand(WsConstant.switchActivateStatus.name(), "", map)));
                sendMap.put(contactsVo.getUserName(), contactsVo);
            }
            isInGrouping = true;
            for (ContactsVo contactsVo : list) {
                if (sendMap.containsKey(contactsVo.getUserName())) {
                    continue;
                }
                if (contactsVo.getType() == 2) {
                    /*clientInfo=clientAccountMapper.selectCurrentClientInfo(contactsVo.getId().longValue());//查询终端是否在对讲组
                    if(clientInfo==null ){
                        isInGrouping=false;
                    }*/
                    map.put("groupName", talkBackGroup.getName());
                    map.put("groupId", talkBackGroup.getId());
                    map.put("isInGrouping", isInGrouping);
                    ConstantList.sendMessage(contactsVo.getUserName(), JSON.toJSONString(new BaseCommand(WsConstant.switchActivateStatus.name(), "", map)));
                    sendMap.put(contactsVo.getUserName(), contactsVo);
                }
            }
        } catch (Exception e) {
            logger.error("发送切换激活对讲组消息异常", e);
        }
    }

    /**
     * 删除对讲组
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteTalkBack(Long id) {
        TalkBackGroup talkBackGroup = talkBackGroupMapper.selectByPrimaryKey(id);
        if (talkBackGroup.getName().equals("默认分组")) {
            throw new SafetyHatException("不能删除默认分组");
        }
        talkBackGroupMapper.updateByIsLock(id, -1);
    }

    /**
     * 查询对讲组
     */
    @Override
    public PageInfo findTalkBack(Integer projectId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<TalkBackGroupVo> list = talkBackGroupMapper.selectByProjectId(projectId.longValue());
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public List<TalkBackGroupVo> findTalkBack(Integer projectId) {
        return talkBackGroupMapper.selectByProjectId(projectId.longValue());
    }

    @Override
    public List<TalkBackGroupVo> findTalkBackByBusinessId(Long businessId) {
        return talkBackGroupMapper.selectByBusinessId(businessId);
    }

    /**
     * 根据对讲组名称查询
     *
     * @return
     */
    @Override
    public PageInfo findTalkBackByName(Integer projectId, String name, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<TalkBackGroupVo> list = talkBackGroupMapper.selectByProjectIdAndName(projectId.longValue(), name);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }
}
