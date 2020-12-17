package com.wyfx.business.entity.vo;

/**
 * @author johnson liu
 * @date 2019/11/19
 * @description 对讲组Vo类
 */
public class TalkBackGroupVo {

    private Long id;

    private String userName;

    /**
     * 对讲组名称
     */
    private String name;
    /**
     * 激活状态
     */
    private Integer isActivate;
    /**
     * 对讲记录
     */
    private Integer isRecording;

    /**
     * 成员数
     */
    private Integer memberNum;


    public TalkBackGroupVo(Long id, String name, Integer isActivate, Integer isRecording, Integer memberNum) {
        this.id = id;
        this.name = name;
        this.isActivate = isActivate;
        this.isRecording = isRecording;
        this.memberNum = memberNum;
    }

    public TalkBackGroupVo(Long id, String userName, String name, Integer isActivate, Integer isRecording, Integer memberNum) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.isActivate = isActivate;
        this.isRecording = isRecording;
        this.memberNum = memberNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsActivate() {
        return isActivate;
    }

    public void setIsActivate(Integer isActivate) {
        this.isActivate = isActivate;
    }

    public Integer getIsRecording() {
        return isRecording;
    }

    public void setIsRecording(Integer isRecording) {
        this.isRecording = isRecording;
    }

    public Integer getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(Integer memberNum) {
        this.memberNum = memberNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
