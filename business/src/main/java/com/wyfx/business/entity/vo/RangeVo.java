package com.wyfx.business.entity.vo;

/**
 * @author johnson liu
 * @date 2019/12/2
 * @description 电子围栏列表Vo
 */
public class RangeVo {
    private Long rid;//电子围栏ID

    private String name;//电子围栏名称

    private Integer type;//禁止类型{0:禁止进入;1:禁止离开}

    private Long projectId;

    private Integer fenceId;//百度地图围栏ID

    private Integer onlineClientNum;//当前在线终端数

    private Integer totalClientNum;//已添加终端数量

    public RangeVo() {
    }

    public RangeVo(Long rid, String name, Integer type, Long projectId, Integer fenceId, Integer onlineClientNum, Integer totalClientNum) {
        this.rid = rid;
        this.name = name;
        this.type = type;
        this.projectId = projectId;
        this.fenceId = fenceId;
        this.onlineClientNum = onlineClientNum;
        this.totalClientNum = totalClientNum;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getFenceId() {
        return fenceId;
    }

    public void setFenceId(Integer fenceId) {
        this.fenceId = fenceId;
    }

    public Integer getOnlineClientNum() {
        return onlineClientNum;
    }

    public void setOnlineClientNum(Integer onlineClientNum) {
        this.onlineClientNum = onlineClientNum;
    }

    public Integer getTotalClientNum() {
        return totalClientNum;
    }

    public void setTotalClientNum(Integer totalClientNum) {
        this.totalClientNum = totalClientNum;
    }
}
