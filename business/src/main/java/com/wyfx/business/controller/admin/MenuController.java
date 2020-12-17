package com.wyfx.business.controller.admin;

import com.wyfx.business.controller.BaseController;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.MenuManager;
import com.wyfx.business.entity.vo.MenuManagerVo;
import com.wyfx.business.service.shiro.MenuManagerService;
import com.wyfx.business.service.shiro.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author johnson liu
 * @date 2019/11/4
 * @description 权限管理 updateUserMenuManager
 */
@RestController
@RequestMapping("/menu")
@Api(value = "MenuController", tags = {"菜单控制器"})
public class MenuController extends BaseController {

    @Autowired
    private MenuManagerService meunManagerService;
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 获取用户权限
     * @return
     */
    /*@RequestMapping(value = "/getUserMenus",method = RequestMethod.GET)
    public Object getUserMenus(){
        BusinessUser businessUser=getCurrentUser();
        if(businessUser==null){
            return new MyResponseEntity(ResponseCode.ERROR_SESSION.getValue(),"会话超时，请重新登陆");
        }
        String userName=businessUser.getUserName();
        List<UserRole> list=userRoleService.findByUserName(userName);
        List<RoleMenuVo> roleMenuVoList= meunManagerService.findUserPermissions(list);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(),roleMenuVoList);
    }*/

    /**
     * 添加权限
     * @param menuManagerVo
     * @return
     */
    /*@RequestMapping(value = "/addMenu",method = RequestMethod.POST)
    public Object addMenu(@RequestBody MenuManagerVo menuManagerVo){
        meunManagerService.addMenu(menuManagerVo);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }*/


    /**
     * 更新角色权限
     * @param
     * @return
     */
    /*@RequestMapping(value = "/updateUserMenuManager",method = RequestMethod.POST)
    public Object updateUserMenuManager(@RequestBody String roleMenu){
        JSONObject jsonObject=JSONObject.parseObject(roleMenu);
        int roleId=jsonObject.getInteger("roleId");
        Integer[] menus=null;
        meunManagerService.updateUserMenuManager(roleId,menus);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }*/

    /**
     * 获取某一级的菜单权限
     *
     * @param parentId 菜单父ID
     * @param userName 用户名
     * @return
     */
    @RequestMapping(value = "/getMenus", method = RequestMethod.GET)
    @ApiOperation(value = "获取某一级的菜单权限", httpMethod = "GET", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "上一级菜单Id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String")
    })
    public Object getMenus(@RequestParam(required = false, defaultValue = "0", value = "parentId") int parentId,
                           @RequestParam("userName") String userName) {
        BusinessUser businessUser = getCurrentUser();
        List<MenuManager> list = meunManagerService.getMenus(parentId, businessUser.getBid());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    /**
     * 获取页面按钮权限
     *
     * @param parentId 权限id
     * @param userName
     * @return
     */
    @RequestMapping(value = "/getButtonMenus", method = RequestMethod.GET)
    @ApiOperation(value = "获取页面按钮权限", httpMethod = "GET", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "上一级菜单Id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String")
    })
    public Object getButtonMenus(@RequestParam(required = false, defaultValue = "0", value = "parentId") int parentId,
                                 @RequestParam(required = false, value = "userName") String userName) {
        BusinessUser businessUser = getCurrentUser();
        List<MenuManager> list = meunManagerService.getButtonMenus(parentId, businessUser.getBid());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    /**
     * 权限配置加载权限
     *
     * @param mid    权限ID
     * @param roleId 角色ID
     * @param type   类型 0菜单 1按钮
     * @return
     */
    @RequestMapping(value = "/findPermissions", method = RequestMethod.GET)
    @RequiresPermissions("menu:findPromise")
    @ApiOperation(value = "权限配置加载权限", httpMethod = "GET", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "权限ID", required = true, dataType = "Integer", example = "1"),
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Long", example = "1"),
            @ApiImplicitParam(name = "type", value = "类型:0菜单 1按钮", required = true, dataType = "Integer", example = "0")
    })
    public Object findPermissions(@RequestParam(required = false, defaultValue = "0", value = "mid") int mid,
                                  @RequestParam("roleId") Long roleId,
                                  @RequestParam(value = "type", defaultValue = "0") int type) {
        List<MenuManagerVo> list = meunManagerService.getRolePermissions(mid, getCurrentUser(), roleId, type);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    /**
     * 更新权限
     *
     * @param roleId        角色Id
     * @param menuManagerVo 权限信息[{"parentId":0,"mid":1,"isAllowed":true}]
     * @return
     */
    @RequestMapping(value = "/updatePermissions", method = RequestMethod.POST)
    @RequiresPermissions("menu:editPromise")
    @ApiOperation(value = "更新权限", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Long", example = "1")
    })
    public Object updatePermissions(@RequestParam("roleId") Long roleId, @RequestBody MenuManagerVo[] menuManagerVo) {
        if (roleId == 1 || roleId == 2 || roleId == 3 || roleId == 4) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "默认角色不能重新配置权限");
        }
        meunManagerService.updatePermissions(roleId, menuManagerVo);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }


}
