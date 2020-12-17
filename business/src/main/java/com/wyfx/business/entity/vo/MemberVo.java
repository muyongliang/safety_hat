package com.wyfx.business.entity.vo;

import com.wyfx.business.app.vo.ContactsVo;

import java.util.List;

/**
 * @author johnson liu
 * @date 2019/11/23
 * @description 成员列表
 */
public class MemberVo {
    private Long groupId;
    private String groupName;
    private List<ContactsVo> waitAdd;
    private List<ContactsVo> add;

    public MemberVo() {
    }

    public MemberVo(Long groupId, String groupName, List<ContactsVo> waitAdd, List<ContactsVo> add) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.waitAdd = waitAdd;
        this.add = add;
    }

    public List<ContactsVo> getWaitAdd() {
        return waitAdd;
    }

    public void setWaitAdd(List<ContactsVo> waitAdd) {
        this.waitAdd = waitAdd;
    }

    public List<ContactsVo> getAdd() {
        return add;
    }

    public void setAdd(List<ContactsVo> add) {
        this.add = add;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
