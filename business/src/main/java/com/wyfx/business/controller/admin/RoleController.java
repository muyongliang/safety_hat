package com.wyfx.business.controller.admin;

import com.github.pagehelper.PageInfo;
import com.wyfx.business.config.annotation.AopLog;
import com.wyfx.business.config.annotation.OperationType;
import com.wyfx.business.controller.BaseController;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.Role;
import com.wyfx.business.service.shiro.RoleService;
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

import java.util.Date;

/**
 * @author johnson liu
 * @date 2019/11/2
 * @description 角色管理
 */
@RestController
@RequestMapping("/role")
@Api(value = "RoleController", tags = {"角色控制器"})
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;

    /**
     * 创建角色
     *
     * @param roleName
     * @param description
     * @return
     */
    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    @RequiresPermissions("role:add")
    @AopLog(describe = "创建角色：", targetParamName = "roleName", operationType = OperationType.INSERT)
    @ApiOperation(value = "创建角色", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "description", value = "角色描述", required = true, dataType = "String")
    })
    public Object addRole(String roleName, String description) {

        System.out.println();
        BusinessUser businessUser = getCurrentUser();
        String userName = businessUser.getUserName();
        Role role = new Role();
        role.setRoleName(roleName);
        role.setCreateBy(userName);
        role.setCreateTime(new Date());
        role.setDescription(description);
        Integer bid = businessUser.getBusinessId().intValue();
        role.setBusinessId(bid.longValue());
        roleService.addRole(role);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 编辑角色
     *
     * @param roleName
     * @param description
     * @return
     */
    @RequestMapping(value = "/editRole", method = RequestMethod.POST)
    @AopLog(describe = "编辑角色：", targetParamName = "roleName", operationType = OperationType.UPDATE)
    @RequiresPermissions("role:edit")
    @ApiOperation(value = "编辑角色", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "description", value = "角色描述", required = true, dataType = "String")
    })
    public Object editRole(Integer roleId, String roleName, String description) {
        BusinessUser businessUser = getCurrentUser();
        String userName = (businessUser == null) ? "admin" : businessUser.getUserName();
        Role role = new Role();
        role.setRoleId(roleId.longValue());
        role.setRoleName(roleName);
        role.setUpdateBy(userName);
        role.setModifyTime(new Date());
        role.setDescription(description);
        roleService.editRole(role);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 查询角色详情
     *
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/findDetailById", method = RequestMethod.GET)
    @RequiresPermissions("role:select")
    @ApiOperation(value = "查询角色详情", httpMethod = "GET", produces = "form-data")
    @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Integer")
    public Object findDetailById(Integer roleId) {
        Role role = roleService.findDetailById(roleId.longValue());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), role);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("role:delete")
    @AopLog(describe = "删除角色：", targetParamName = "roleName", operationType = OperationType.DELETE)
    @ApiOperation(value = "删除角色", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Integer")
    public Object delete(Integer roleId, @RequestParam(required = false) String roleName) {
        BusinessUser user = getCurrentUser();
        if (roleId == 1 || roleId == 2 || roleId == 3 || roleId == 4) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "禁止删除默认角色");
        }
        roleService.deleteRole(user, roleId.longValue());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 分页查询角色信息/根据角色名称查询
     *
     * @return
     */
    @RequestMapping(value = "/findRoles", method = RequestMethod.GET)
    @RequiresPermissions("role:view")
    @ApiOperation(value = "分页查询角色信息/根据角色名称查询", httpMethod = "GET", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = true, dataType = "Integer")
    })
    public Object findRoles(@RequestParam(required = false, value = "roleName") String roleName,
                            @RequestParam(required = false, defaultValue = "1", value = "pageNum") Integer pageNum,
                            @RequestParam(required = false, defaultValue = "10", value = "pageSize") Integer pageSize) {
        BusinessUser user = getCurrentUser();
        PageInfo<Role> pageInfo = roleService.findDetailByRoleName(user.getBusinessId(), roleName, pageNum, pageSize);
        /*PageInfo<Role> pageInfo= roleService.findDetailByRoleName(roleName,pageNum,pageSize);*/
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 检查角色名称是否重名
     *
     * @param roleName
     * @return
     */
    @RequestMapping(value = "/checkRoleName", method = RequestMethod.GET)
    @ApiOperation(value = "检查角色名称是否重名", httpMethod = "GET", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String")
    })
    public Object checkRoleName(String roleName) {
        BusinessUser user = getCurrentUser();
        boolean flag = roleService.checkRoleName(user.getBusinessId(), roleName);
        if (flag) {
            return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
        }
        return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "角色已存在");
    }

}
