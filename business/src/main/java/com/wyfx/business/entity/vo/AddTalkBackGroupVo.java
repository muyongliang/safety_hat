package com.wyfx.business.entity.vo;

/**
 * @author johnson liu
 * @date 2019/11/20
 * @description 添加对讲组
 */
public class AddTalkBackGroupVo {
    private String name;
    private Object[] members;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object[] getMembers() {
        return members;
    }

    public void setMembers(Object[] members) {
        this.members = members;
    }
}
