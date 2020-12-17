package com.wyfx.business.config.annotation;

/**
 * @author Administrator
 */
public enum OperationType {
    /**
     *
     */
    INSERT(1, "添加"),
    DELETE(2, "删除"),
    UPDATE(3, "更新"),
    CLEAR(4, "清空"),
    /**
     * 启用/禁用账号
     */
    /*LOCK(5,"禁用"),
    UNLOCK(6,"启用"),*/
    /**
     * 移动账号到某一项目
     */
    MOVE(7, "移动"),
    /*EXPORT(8,"导出"),
    IMPORT(9,"导入"),*/
    /*LOGIN(10,"登录"),*/
    /*LOGOUT(11,"登出"),*/
    /*SELECT(12,"查询"),*/
    RESET(13, "重置密码"),
    All(0, "全部");

    private String name;
    private Integer type;

    OperationType(Integer type, String name) {
        this.name = name;
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
