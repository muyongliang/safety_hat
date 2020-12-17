package com.wyfx.business.entity.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author johnson liu
 * @date 2019/11/4
 * @description MenuManager的VO类, 接收前端请求
 */
@ApiModel("权限配置信息")
public class MenuManagerVo {
    @ApiModelProperty(value = "角色Id", example = "1")
    private Long roleId;
    @ApiModelProperty(value = "权限Id", example = "0")
    private Long mid;
    @ApiModelProperty(value = "上一级权限Id", example = "0")
    private Long parentId;
    @ApiModelProperty("菜单名称")
    private String name;
    @ApiModelProperty("菜单Url链接")
    private String url;
    @ApiModelProperty("菜单类型:0菜单 1按钮")
    private Integer type;
    @ApiModelProperty("排序字段")
    private Integer orderNum;
    @ApiModelProperty("菜单标识")
    private String remark;
    @ApiModelProperty("是否具有该权限：true有，false无")
    private Boolean isAllowed;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public Boolean getIsAllowed() {
        return isAllowed;
    }

    public void setIsAllowed(Boolean isAllowed) {
        this.isAllowed = isAllowed;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Boolean getAllowed() {
        return isAllowed;
    }

    public void setAllowed(Boolean allowed) {
        isAllowed = allowed;
    }
}
