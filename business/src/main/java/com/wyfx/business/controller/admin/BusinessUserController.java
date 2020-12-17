package com.wyfx.business.controller.admin;

import com.alibaba.fastjson.JSON;
import com.wyfx.business.config.annotation.AopLog;
import com.wyfx.business.config.annotation.OperationType;
import com.wyfx.business.controller.BaseController;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.entity.BusinessInfo;
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.ZidianSetting;
import com.wyfx.business.service.common.IBusinessInfoService;
import com.wyfx.business.service.shiro.BusinessUserService;
import com.wyfx.business.service.sys.ZidianServer;
import com.wyfx.business.utils.MD5Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author johnson liu
 * @date 2019/11/25
 * @description 用户中心
 */
@RestController
@RequestMapping("/user")
@Api(value = "BusinessUserController", tags = {"调度员控制器"})
public class BusinessUserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BusinessUserController.class);
    @Autowired
    private BusinessUserService businessUserService;
    @Autowired
    private ZidianServer zidianServer;
    @Autowired
    private IBusinessInfoService iBusinessInfoService;

    /**
     * 修改姓名
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/modifyName", method = RequestMethod.POST)
    @AopLog(describe = "修改账户姓名为：", targetParamName = "name", operationType = OperationType.UPDATE)
    @ApiOperation(value = "修改姓名", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParam(name = "name", value = "用户名姓", required = true, dataType = "String")
    public Object modifyName(String name) {
        BusinessUser businessUser = getCurrentUser();
        businessUser.setName(name);
        try {
            businessUserService.updateNameOrPwd(businessUser.getBid(), name, null);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @RequestMapping(value = "/modifyPassWord", method = RequestMethod.POST)
    @AopLog(describe = "修改当前账户密码", operationType = OperationType.UPDATE)
    @ApiOperation(value = "修改当前账户密码", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword", value = "旧密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, dataType = "String")
    })
    public Object modifyPassWord(String oldPassword, String newPassword) {
        BusinessUser businessUser = getCurrentUser();
        if (!MD5Util.encrypt(businessUser.getUserName(), oldPassword).equals(businessUser.getPassword())) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "原密码不正确");
        }
        businessUser.setPassword(MD5Util.encrypt(businessUser.getUserName(), newPassword));
        try {
            businessUserService.updateNameOrPwd(businessUser.getBid(), null, newPassword);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 总后台添加企业时向企业后台插入企业相关信息
     * create by wsm on 2019-12-5
     *
     * @param json
     * @return
     */
    @PostMapping("/handleAddBInfoAndBUser")
    @AopLog(describe = "创建企业管理员账号：", targetParamName = "json", operationType = OperationType.INSERT)
    @ApiOperation(value = "创建企业管理员账号", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "json", value = "管理员账号相关信息", required = true, dataType = "String")
    })
    public String handleAddBInfoAndBUser(@RequestBody String json) {
        logger.info(" 总后台添加企业时向企业后台插入企业相关信息");
        BusinessInfo businessInfo = JSON.parseObject(json, BusinessInfo.class);
        //插入企业基本信息
        boolean b = iBusinessInfoService.addBusinessInfo(businessInfo);
        //插入企业登录账户信息
        BusinessUser businessUser = new BusinessUser();
        businessUser.setUserName(businessInfo.getMainAccount());//账号名
        businessUser.setPassword(MD5Util.encrypt(businessInfo.getMainAccount(), businessInfo.getMainAccount()));//根据用户名密码加密返回加密后的密码
        businessUser.setToken(businessInfo.getToken());
        businessUser.setBusinessId(businessInfo.getBusinessId());//所属企业的id 即企业信息表(businessInfo)的id
        businessUser.setUserType(0);//企业管理员
        businessUser.setCreatePerson("总后台添加");
        businessUser.setName(businessInfo.getBusinessName());//企业名
        businessUser.setCreateTime(new Date());
        logger.info("总后台添加企业的信息===" + businessInfo);
        try {
            businessUserService.addBusinessUser(businessUser);
            //ZidianSetting zidian=new ZidianSetting(zidianType.longValue(),workName,status,color,businessUser.getBid());
            ZidianSetting moren = new ZidianSetting(0L, "默认工种", 0, "#00cc00", businessUser.getBusinessId());
            zidianServer.addWorkType(moren);
        } catch (Exception e) {
            logger.error("总后台添加账号异常:", e);
            return "false";
        }
        if (!b) {
            logger.info("总后台添加企业出现异常2");
            return " false";
        }
        return "true";
    }

    /**
     * 总后台更新企业信息
     * create by wsm on 2019-12-13
     *
     * @param businessInfoJson
     * @return
     */
    @PostMapping("/handleUpdateBInfoAndBUser")
    public String handleUpdateBInfoAndBUser(@RequestBody String businessInfoJson) {
        logger.info("总后台更新企业信息");
        BusinessInfo businessInfo = JSON.parseObject(businessInfoJson, BusinessInfo.class);
        boolean b = iBusinessInfoService.updateBusinessInfo(businessInfo);
        BusinessUser businessUser = new BusinessUser();
        businessUser.setUserName(businessInfo.getMainAccount());
        businessUser.setName(businessInfo.getBusinessName());
        businessUser.setModifyTime(new Date());
        businessUser.setModifyPerson("总后台修改");
        businessUser.setToken(businessInfo.getToken());
        boolean b1 = businessUserService.updateBusinessUser(businessUser);
        if (b && b1) {
            return " true";
        }
        return "false";
    }

    /**
     * 总后台 ---重置企业密码
     *
     * @param tokenJson
     * @return
     */
    @PostMapping("/handleRestPassWord")
    public String handleRestPassWord(@RequestBody String tokenJson) {
        logger.info("总后台 ---重置企业密码");
        Map map = JSON.parseObject(tokenJson, Map.class);
        BusinessUser businessUser = new BusinessUser();
        businessUser.setToken(map.get("token").toString());//设置企业的token
        String mainAccount = map.get("mainAccount").toString();
        //重置密码
        businessUser.setPassword(MD5Util.encrypt(mainAccount, mainAccount));
        //更新密码
        boolean b = businessUserService.updateBusinessUser(businessUser);
        if (b) {
            return " true";
        }
        return "false";
    }

    /**
     * 总后台---暂停企业
     *
     * @param json
     * @return
     */
    @PostMapping("/handlePauseOperation")
    public String handlePauseOperation(@RequestBody String json) {
        logger.info("总后台---暂停企业");
        BusinessInfo businessInfo = new BusinessInfo();
        businessInfo.setToken(JSON.parse(json).toString());
        businessInfo.setStatus(1);//合作状态{0:合作中;1:暂停合作}
        boolean b = iBusinessInfoService.updateBusinessInfo(businessInfo);
        if (b) {
            return " true";
        }
        return "false";
    }

    /**
     * 总后台--重启企业合作状态
     *
     * @param json
     * @return
     */
    @PostMapping("/handleRestartOperation")
    public String handleRestartOperation(@RequestBody String json) {
        logger.info("总后台--重启企业合作状态");
        BusinessInfo businessInfo = new BusinessInfo();
        businessInfo.setToken(JSON.parse(json).toString());
        businessInfo.setStatus(0);//合作状态{0:合作中;1:暂停合作}
        boolean b = iBusinessInfoService.updateBusinessInfo(businessInfo);
        if (b) {
            return " true";
        }
        return "false";
    }

    /**
     * 总后台---到期自动批量暂停企业合作
     *
     * @return
     */
    @PostMapping("/handleAutoPauseOperation")
    public String handleAutoPauseOperation(@RequestBody List<String> tokens) {
        logger.info("总后台---到期自动批量暂停企业合作 token" + tokens);
        boolean b = iBusinessInfoService.updateBusinessStatusByTokens(tokens);
        if (b) {
            return " true";
        }
        return "false";
    }


}
