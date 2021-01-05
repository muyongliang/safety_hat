package com.wyfx.business.entity.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author johnson liu
 * @date 2019/11/4
 * @description MenuManager的VO类, 接收前端请求
 */
@ApiModel("权限配置信息")
@Data
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

}
