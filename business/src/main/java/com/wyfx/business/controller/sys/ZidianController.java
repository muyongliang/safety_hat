package com.wyfx.business.controller.sys;

import com.github.pagehelper.PageInfo;
import com.wyfx.business.config.annotation.AopLog;
import com.wyfx.business.config.annotation.OperationType;
import com.wyfx.business.controller.BaseController;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.ZidianSetting;
import com.wyfx.business.service.sys.ZidianServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author johnson liu
 * @date 2019/11/6
 * @description 字典配置控制器
 */
@RestController
@RequestMapping("/system/zidian")
@Api(value = "ZidianController", tags = {"字典配置"})
public class ZidianController extends BaseController {

    @Autowired
    private ZidianServer zidianServer;

    /**
     * 新增工种
     *
     * @param zidianType 字典类型
     * @param workName   工种名称
     * @param status     启用状态
     * @param color      显示色值
     * @return
     */
    @RequestMapping(value = "/addWorkType", method = RequestMethod.POST)
    @AopLog(describe = "添加工种：", targetParamName = "workName", operationType = OperationType.INSERT)
    @RequiresPermissions("zidian:addWorkType")
    @ApiOperation(value = "新增工种", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "zidianType", value = "字典类型", required = true, dataType = "Integer", example = "0"),
            @ApiImplicitParam(name = "workName", value = "工种名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "启用状态", required = true, dataType = "Integer", example = "0"),
            @ApiImplicitParam(name = "color", value = "显示色值", required = true, dataType = "String"),
    })
    public Object addWorkType(Integer zidianType, String workName, Integer status, String color) {
        BusinessUser businessUser = getCurrentUser();
        ZidianSetting zidianSetting = zidianServer.findByName(businessUser.getBusinessId(), workName);
        if (zidianSetting != null) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "工种已存在");
        }
        ZidianSetting zidian = new ZidianSetting(zidianType.longValue(), workName, status, color, businessUser.getBusinessId());
        zidianServer.addWorkType(zidian);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 编辑工种
     *
     * @param zid      工种ID
     * @param workName 工种名称
     * @param status   启用状态
     * @param color    显示色值
     * @return
     */
    @RequestMapping(value = "/updateWorkType", method = RequestMethod.POST)
    @AopLog(describe = "编辑工种：", targetParamName = "workName", operationType = OperationType.UPDATE)
    @RequiresPermissions("zidian:edit")
    @ApiOperation(value = "编辑工种", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "zid", value = "工种ID", required = true, dataType = "Integer", example = "0"),
            @ApiImplicitParam(name = "workName", value = "工种名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "启用状态", required = true, dataType = "Integer", example = "0"),
            @ApiImplicitParam(name = "color", value = "显示色值", required = true, dataType = "String"),
    })
    public Object updateWorkType(Integer zid, String workName, Integer status, String color) {
        ZidianSetting zidian = new ZidianSetting();
        zidian.setZid(zid.longValue());
        zidian.setColor(color);
        zidian.setStatus(status);
        zidian.setName(workName);
        zidianServer.updateWorkType(zidian);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 更新字典启用状态
     *
     * @param zid
     * @param status
     * @return
     */
    @RequestMapping(value = "/updateWorkTypeStatus", method = RequestMethod.POST)
    @RequiresPermissions("zidian:changeStatus")
    @AopLog(describe = "更新字典的启用状态:", targetParamName = "name", operationType = OperationType.UPDATE)
    @ApiOperation(value = "更新字典启用状态", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "zid", value = "工种ID", required = true, dataType = "Integer", example = "0"),
            @ApiImplicitParam(name = "status", value = "启用状态", required = true, dataType = "Integer", example = "0")
    })
    public Object updateWorkTypeStatus(Integer zid, Integer status, @RequestParam(required = false) String name) {
        zidianServer.updateWorkTypeStatus(zid, status, getCurrentUser().getBusinessId().intValue());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 获取字典信息
     *
     * @param zidianType
     * @param pageSize
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "/getZidianInfo", method = RequestMethod.POST)
    /*@RequiresPermissions("zidian:view")*/
    @ApiOperation(value = "获取字典信息", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "zidianType", value = "字典类型", required = true, dataType = "Integer", example = "0"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer", example = "0"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = true, dataType = "Integer", example = "0")
    })
    public Object getZidianInfo(Integer zidianType, Integer pageSize, Integer pageNum) {
        BusinessUser businessUser = getCurrentUser();
        PageInfo<ZidianSetting> pageInfo = zidianServer.findAll(pageSize, pageNum, zidianType, businessUser.getBusinessId());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 获取工种列表
     *
     * @param zidianType
     * @return
     */
    @RequestMapping(value = "/getWorkTypeList", method = RequestMethod.GET)
    @ApiOperation(value = "获取工种列表", httpMethod = "GET", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "zidianType", value = "字典类型", required = true, dataType = "Integer", example = "0")
    })
    public Object getWorkTypeList(Integer zidianType) {
        BusinessUser businessUser = getCurrentUser();
        List<ZidianSetting> list = zidianServer.getWorkTypeList(zidianType, businessUser.getBusinessId());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    /**
     * 改变字典排序
     *
     * @param zid      字典ID
     * @param orderNum 字典排序字段
     * @param moveType 移动类型{0:升序(增加排序值);1:降序(减少排序)}
     * @return
     */
    @RequestMapping(value = "/changeZidianOrderNum", method = RequestMethod.POST)
    @RequiresPermissions("zidian:order")
    @ApiOperation(value = "改变字典排序", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "zid", value = "工种ID", required = true, dataType = "Integer", example = "0"),
            @ApiImplicitParam(name = "orderNum", value = "字典排序字段", required = true, dataType = "Integer", example = "0"),
            @ApiImplicitParam(name = "moveType", value = "移动类型{0:升序(增加排序值);1:降序(减少排序)}", required = true, dataType = "Integer", example = "0")
    })
    public Object changeZidianOrderNum(Integer zid, Integer orderNum, Integer moveType) {
        BusinessUser businessUser = getCurrentUser();
        try {
            zidianServer.changeZidianOrderNum(zid, orderNum, moveType, businessUser.getBusinessId());
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

}
