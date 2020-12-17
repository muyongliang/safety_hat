package com.wyfx.business.app.service;

import com.github.pagehelper.PageInfo;
import com.wyfx.business.app.vo.ContactsVo;
import com.wyfx.business.app.vo.MemberDetailVo;
import com.wyfx.business.entity.GroupInfo;
import com.wyfx.business.entity.vo.MemberVo;

import java.util.List;

public interface ContactService {

    /**
     * 分页查询通讯录中的调度员和终端账号
     *
     * @param projectId
     * @return
     */
    PageInfo<ContactsVo> findContactByProjectId(Long bid, Integer projectId, Integer pageSize, Integer pageNum);

    /**
     * 根据姓名查询
     *
     * @return
     */
    PageInfo<ContactsVo> findContactByName(Integer projectId, String name, Integer pageSize, Integer pageNum);

    /**
     * 根据工种查询
     *
     * @return
     */
    PageInfo<ContactsVo> findContactByWorkType(Integer projectId, Integer workTye, Integer pageSize, Integer pageNum);

    PageInfo<ContactsVo> findContactGroupMember(Long groupId, Integer pageSize, Integer pageNum);

    /**
     * 根据终端ID查询终端详细信息
     *
     * @param clientId
     * @return
     */
    Object findByClientId(Integer clientId);

    /**
     * 获取企业成员语音等功能的权限
     *
     * @return
     */
    Object getFunction();

    /**
     * 添加通讯录群组
     */
    void addContactGroup(Integer projectId, MemberVo memberVo);

    /**
     * 更新通讯录群组
     */
    void updateContactGroup(Long projectId, MemberVo memberVo);

    void deleteContactGroup(Integer groupId);

    /**
     * 获取通讯录成员列表
     *
     * @param groupId
     * @return
     */
    MemberVo getMemberList(Long groupId, Long projectId, Long bid) throws Exception;

    List<GroupInfo> findByGroupName(Long projectId, String groupName);

    MemberDetailVo findMemberInfo(Long userId, Integer type);
}
