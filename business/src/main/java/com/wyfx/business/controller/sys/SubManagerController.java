package com.wyfx.business.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.wyfx.business.config.annotation.AopLog;
import com.wyfx.business.config.annotation.OperationType;
import com.wyfx.business.controller.BaseController;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.controller.commons.SafetyHatException;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.vo.SubManagerVo;
import com.wyfx.business.service.shiro.BusinessUserService;
import com.wyfx.business.service.shiro.RoleService;
import com.wyfx.business.service.shiro.UserRoleService;
import com.wyfx.business.service.sys.SubManagerService;
import com.wyfx.business.utils.AccountUtil;
import com.wyfx.business.utils.MD5Util;
import com.wyfx.business.utils.UserTypeAndStatus;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 子账号管理
 * create by wsm on 2019-11-29
 */
@RequestMapping("/subManager")
@RestController
public class SubManagerController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(SubManagerController.class);
    @Autowired
    private BusinessUserService businessUserService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private SubManagerService subManagerService;
    @Autowired
    private RoleService roleService;

    /**
     * 添加子账号
     *
     * @param subManagerVo
     * @return
     */
    @PostMapping("/handleAddSubManager")
    @AopLog(describe = "创建子账号：", targetParamName = "subManagerVo", operationType = OperationType.INSERT)
    @RequiresPermissions("subAdministrator:add")
    public MyResponseEntity handleAddSubManager(SubManagerVo subManagerVo, String roles) {
        logger.info("添加子账号" + subManagerVo + "角色列表=" + roles);
        if (!AccountUtil.vilicationPhone(subManagerVo.getMobile())) {
            return new MyResponseEntity(500, "电话号码格式错误");
        }
        if (roles == null || "".equals(roles)) {
            return new MyResponseEntity(500, "请选择角色");
        }
        BusinessUser currentUser = getCurrentUser();
//        if (currentUser.getUserType() != 0) {
//            return new MyResponseEntity(ResponseCode.ERROR_IDENTIFY.getValue(), "不是管理员没有分配子管理员的权限");
//        }

        //账号去重 在业务层实现了
        BusinessUser businessUser = new BusinessUser();
        //BeanUtils.copyProperties(subManagerVo, businessUser);
        businessUser.setUserName(subManagerVo.getUsername());
        businessUser.setName(subManagerVo.getName());
        //通过过用户名和密码加密后存入数据库  分配子账号的用户名和密码一致
        businessUser.setPassword(MD5Util.encrypt(subManagerVo.getUsername(), subManagerVo.getUsername()));//初始密码与账号相同

        businessUser.setMobile(subManagerVo.getMobile());
        businessUser.setCreateTime(new Date());
        businessUser.setCreatePerson(currentUser.getName());
        businessUser.setBusinessId(currentUser.getBusinessId());
        businessUser.setUserType(UserTypeAndStatus.OTHER_MANAGER);
        businessUser.setStatus(UserTypeAndStatus.account_Enable_status);
        businessUser.setToken(currentUser.getToken());
        try {
            JSONObject jsonObject = JSONObject.parseObject(roles);
            //角色列表
            Object[] members = jsonObject.getJSONArray("role").toArray();
            businessUserService.addSubManager(businessUser, members);
        } catch (SafetyHatException e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        } catch (Exception e) {
            logger.error("添加子管理员失败", e);
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "添加子管理员失败");
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "添加子管理员成功");
    }

    /**
     * 查询所有系统分配的角色列表
     *
     * @return
     */
    @GetMapping("/handleSelectAllRole")
    public MyResponseEntity handleSelectAllRole() {
        BusinessUser currentUser = getCurrentUser();
        List<Map> mapList = roleService.selectAllRole(currentUser.getBusinessId());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), mapList);
    }


    /**
     * 通过状态查询子账号
     *
     * @param state
     * @return
     */
    @GetMapping("/handleSelectByState")
    @RequiresPermissions("subAdministrator:select")
    public MyResponseEntity handleSelectByState(String state, Integer pageNum, Integer pageSize) {
        BusinessUser currentUser = getCurrentUser();
        Map map = subManagerService.selectSubManagerByState(
                (state == null || "".equals(state)) ? null : Integer.parseInt(state), currentUser.getBusinessId(), pageNum, pageSize);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), map);
    }

    /**
     * 通过id查询 子账号
     *
     * @param bid
     * @return
     */
    @GetMapping("/handleSelectById")
    @RequiresPermissions("subAdministrator:view")
    public MyResponseEntity handleSelectById(Integer bid) {
        System.out.println("bid================" + bid);
        Map map = subManagerService.selectSubManagerById(bid);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), map);
    }


    /**
     * 编辑 禁用 重置密码
     *
     * @param subManagerVo
     * @param roles
     * @return
     */
    @PostMapping("/handleEditSubManager")
    @RequiresPermissions("subAdministrator:edit")
    public MyResponseEntity handleEditSubManager(SubManagerVo subManagerVo, String roles) {
        logger.info("编辑 禁用 重置密码");
        boolean b = subManagerService.editSubManager(subManagerVo, roles);
        if (!b) {
            return new MyResponseEntity(501, "更新失败，联系管理员");
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }


    /**
     * 重置密码
     *
     * @param subManagerVo
     * @return
     */
    @GetMapping("/handleResetPwd")
    @RequiresPermissions("subAdministrator:resetPassword")
    public MyResponseEntity handleResetPwd(SubManagerVo subManagerVo) {
        logger.info("重置密码");
        boolean b = subManagerService.resetPwd(subManagerVo);
        if (!b) {
            return new MyResponseEntity(501, "更新数据失败,联系管理员");
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 改变子账号启用与禁止状态
     *
     * @param subManagerVo
     * @return
     */
    @GetMapping("/handleUpdateStatus")
    @RequiresPermissions("subAdministrator:changeStatus")
    public MyResponseEntity handleUpdateStatus(SubManagerVo subManagerVo) {
        boolean b = subManagerService.updateStatus(subManagerVo);
        if (!b) {
            return new MyResponseEntity(501, "更新数据失败,联系管理员");
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 通过账号 姓名 电话检索
     *
     * @param subManagerVo
     * @return
     */
    @GetMapping("/handleSelectByCondition")
    @RequiresPermissions("subAdministrator:select")
    public MyResponseEntity handleSelectByCondition(SubManagerVo subManagerVo, Integer pageNum, Integer pageSize) {
        System.out.println("sublist=" + subManagerVo);
        BusinessUser user = getCurrentUser();
        Map map = subManagerService.selectByCondition(subManagerVo, user.getBusinessId(), pageNum, pageSize);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), map);
    }


}
