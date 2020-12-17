package com.wyfx.business.constant;

public enum RecordTypeEnum {

    broadCast(1, "广播记录"),
    talkBack(2, "对讲记录"),
    talkBackDetail(3, "对讲记录明细"),
    phone(4, "电话录音记录"),
    patrol(5, "巡检报告");

    private Integer type;
    private String name;

    RecordTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
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
