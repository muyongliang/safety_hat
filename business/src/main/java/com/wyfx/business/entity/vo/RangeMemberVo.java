package com.wyfx.business.entity.vo;

import com.wyfx.business.app.vo.ContactsVo;

import java.util.List;

/**
 * @author johnson liu
 * @date 2019/12/1
 * @description 围栏终端
 */
public class RangeMemberVo {
    private Long rid;
    private String name;//电子围栏名称
    private Integer fenceId;//百度云的围栏ID
    private List<ContactsVo> waitAdd;
    private List<ContactsVo> add;

    public RangeMemberVo() {
    }

    public RangeMemberVo(Long rid, String name, Integer fenceId, List<ContactsVo> waitAdd, List<ContactsVo> add) {
        this.rid = rid;
        this.name = name;
        this.fenceId = fenceId;
        this.waitAdd = waitAdd;
        this.add = add;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFenceId() {
        return fenceId;
    }

    public void setFenceId(Integer fenceId) {
        this.fenceId = fenceId;
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
}
