package com.wyfx.business.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.alarmRange.util.HttpClientUtil;
import com.wyfx.business.config.annotation.AopLog;
import com.wyfx.business.config.annotation.OperationType;
import com.wyfx.business.controller.BaseController;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.controller.commons.SafetyHatException;
import com.wyfx.business.entity.*;
import com.wyfx.business.entity.vo.ClientAccountSelectVo;
import com.wyfx.business.entity.vo.ClientAccountVo;
import com.wyfx.business.entity.vo.MyClientAccountVo;
import com.wyfx.business.service.*;
import com.wyfx.business.service.common.IExcelService;
import com.wyfx.business.service.common.ILogService;
import com.wyfx.business.service.shiro.BusinessUserService;
import com.wyfx.business.service.sys.SystemServer;
import com.wyfx.business.utils.AccountUtil;
import com.wyfx.business.utils.MD5Util;
import com.wyfx.business.utils.UserTypeAndStatus;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author johnson liu
 * @date 2019/11/7
 * @description 终端账号控制器
 */
@RestController
@RequestMapping("/client")
public class ClientAccountController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ClientAccountController.class);
    @Autowired
    private ClientAccountService clientAccountService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private BusinessUserService businessUserService;
    @Autowired
    private TalkBackService talkBackService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private TalkBackGroupMemberService talkBackGroupMemberService;
    @Autowired
    private ILogService iLogService;
    @Autowired
    private SystemServer systemServer;
    @Autowired
    private IExcelService iExcelService;

    @RequestMapping(value = "/findDetailById", method = RequestMethod.GET)
    public Object findDetailById(Integer clientId) {
        if (clientId == null || clientId == 0) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "参数不能为空");
        }

        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    @RequestMapping(value = "/checkAccount", method = RequestMethod.GET)
    public Object checkAccount(String userName) {
        if (userName == null || userName.equals("")) {
            return new MyResponseEntity(ResponseCode.ERROR_PARAM.getValue(), "参数不能为空");
        }
        if (!AccountUtil.checkAccountName(userName)) {
            return new MyResponseEntity(ResponseCode.ERROR_PARAM.getValue(), "输入账号/姓名不符合要求");
        }
        BusinessUser user = getCurrentUser();
        boolean flag = businessUserService.checkAccount(userName, user.getBusinessId());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), flag);
    }

    /**
     * 查询终端用户当前的所在对讲组信息: 对讲组名称，终端用户姓名，设备编号
     *
     * @return
     */
    @RequestMapping(value = "/getCurrentClientInfo", method = RequestMethod.GET)
    public Object getCurrentClientInfo() {
        BusinessUser user = getCurrentUser();
        Map map = clientAccountService.getCurrentClientInfo(user.getBid());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), map);
    }

    /**
     * 添加终端账户,并将账户加入默认对讲组中
     *
     * @param clientAccountVo
     * @return
     */
    @RequestMapping(value = "/addClientAccount", method = RequestMethod.POST)
    @RequiresPermissions("client:add")
    @AopLog(describe = "创建终端账号：", targetParamName = "clientAccountVo", operationType = OperationType.INSERT)
    public Object addClientAccount(@RequestBody @Valid ClientAccountVo clientAccountVo) {
        BusinessUser user = getCurrentUser();
        if (!clientAccountService.isAllowAddClientAccount(user.getToken())) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "已超过终端账号限制");
        }
        if (!AccountUtil.vilicationPhone(clientAccountVo.getTel())) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "电话号码格式错误");
        }
        if (clientAccountVo.getDeviceId() != null) {
            List<ClientAccount> list = clientAccountService.findByDeviceId(clientAccountVo.getDeviceId().intValue());
            if (list.size() > 0) {
                return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "该账号已经绑定了设备,请重新选择");
            }
        }
        ProjectInfo projectInfo = projectService.findByProjectId(clientAccountVo.getProjectId());
        if (projectInfo.getStatus() == 1) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "该项目已结束,不能创建账号");
        }
        ClientAccount clientAccount = new ClientAccount();
        BeanUtils.copyProperties(clientAccountVo, clientAccount);

        BusinessUser businessUser = new BusinessUser();
        BeanUtils.copyProperties(clientAccountVo, businessUser);
        businessUser.setUserName(clientAccountVo.getAccount());
        businessUser.setPassword(MD5Util.encrypt(clientAccountVo.getAccount(), clientAccountVo.getPassword()));
        businessUser.setMobile(clientAccountVo.getTel());
        businessUser.setCreateTime(new Date());
        businessUser.setCreatePerson(user.getUserName());
        businessUser.setBusinessId(user.getBusinessId());
        businessUser.setUserType(UserTypeAndStatus.CLIENT_ACCOUNT);
        //查询该企业下的终端数量是否超过限制数量
        int status = (!clientAccountService.isAllowChangeStatus(user.getToken())) ? UserTypeAndStatus.account_not_Enable_status : UserTypeAndStatus.account_Enable_status;
        businessUser.setStatus(status);
        businessUser.setZidianId(clientAccountVo.getWorkId());
        // 查询字典配置中的终端字典id
        //add by wsm
        businessUser.setToken(user.getToken());
        try {
            businessUserService.addBusinessUser(businessUser);
            if (clientAccountVo.getDeviceId() != null && clientAccountVo.getDeviceId() != 0) {
                //更新device的状态
                DeviceInfo deviceInfo = new DeviceInfo(clientAccountVo.getDeviceId(), null, null, null, 1, null, null, null, null, null, null, null);
                deviceService.updateDeviceInfo(clientAccountVo.getDeviceId().intValue(), deviceInfo);
            }
        } catch (SafetyHatException e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "添加失败");
        }

        /*以下是之前的代码*/

        clientAccount.setBusinessId(user.getBusinessId());
        clientAccount.setProjectId(user.getProjectId());
        clientAccount.setStatus(0);
        clientAccount.setBid(businessUser.getBid());
        clientAccountService.addClientAccount(clientAccount);

        //添加日志   accountType 账号类型{1:调度员,0:企业管理员;2:终端账号;3:其他管理员账号}  actionType  操作类型 增加 1 删除2 更新3
        Log log = new Log(null, 2, user.getName(), 1, "添加", "添加终端账号：" + clientAccountVo.getAccount(), new Date(), businessUser.getBusinessId(), businessUser.getBid());
        boolean b = iLogService.addLog(log);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 同步视频参数
     *
     * @return
     */

    public Object synchronizeVideoParam(String resolution) {
        try {
            BusinessUser user = getCurrentUser();
            systemServer.synchronizeVideoParam2(user.getBusinessId(), resolution);
        } catch (SafetyHatException e) {
            e.printStackTrace();
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "操作失败");
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "发送视频参数同步命令成功");
    }

    /**
     * 编辑终端账号
     *
     * @param clientAccountVo
     * @return
     */
    @RequestMapping(value = "/editClientAccount", method = RequestMethod.POST)
    @RequiresPermissions("client:edit")
    @AopLog(describe = "编辑终端账号：", targetParamName = "clientAccountVo", operationType = OperationType.UPDATE)
    public Object editClientAccount(@RequestBody @Valid ClientAccountVo clientAccountVo) {//endBinding
        BusinessUser user = getCurrentUser();
        if (!AccountUtil.vilicationPhone(clientAccountVo.getTel())) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "电话号码格式错误");
        }
        if (clientAccountVo.getDeviceId() != null && clientAccountVo.getDeviceId() != 0) {
            List<ClientAccount> list = clientAccountService.findByDeviceId(clientAccountVo.getDeviceId().intValue());
            if (list.size() > 0 && list.get(0).getBid() != clientAccountVo.getClientId()) {
                return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "该账号已经绑定了设备,请重新选择");
            }
        }
        BusinessUser businessUser = new BusinessUser();
        BeanUtils.copyProperties(clientAccountVo, businessUser);
        businessUser.setBid(clientAccountVo.getClientId());
        businessUser.setPassword(MD5Util.encrypt(clientAccountVo.getAccount(), clientAccountVo.getPassword()));
        businessUser.setMobile(clientAccountVo.getTel());
        businessUser.setModifyTime(new Date());
        businessUser.setModifyPerson(user.getName());
        businessUser.setZidianId(clientAccountVo.getWorkId());
        try {
            businessUserService.addBusinessUser(businessUser);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        }

        String resolution = clientAccountVo.getResolution();
        ClientAccount clientAccount = new ClientAccount();
        BeanUtils.copyProperties(clientAccountVo, clientAccount);

        /*clientAccount.setClientId(null);*/
        clientAccount.setBid(clientAccountVo.getClientId());
        clientAccountService.updateClientAccountByBid(clientAccountVo.getClientId().intValue(), clientAccount);
        if (clientAccountVo.getDeviceId() == null || clientAccountVo.getDeviceId() == 0) {//取消绑定
            //更新device的状态
            Long endBinding = clientAccountVo.getEndBinding();
            if (endBinding != null || endBinding != 0) {
                DeviceInfo deviceInfo = new DeviceInfo(endBinding, null, null, null, 0, null, null, null, null, null, null, null);
                deviceService.updateDeviceInfo(endBinding.intValue(), deviceInfo);
            }
        } else {
            DeviceInfo deviceInfo = new DeviceInfo(clientAccountVo.getDeviceId(), null, null, null, 1, null, null, null, null, null, null, null);
            deviceService.updateDeviceInfo(clientAccountVo.getDeviceId().intValue(), deviceInfo);
        }
        synchronizeVideoParam(resolution);
        //添加日志   accountType 账号类型{1:调度员,0:企业管理员;2:终端账号;3:其他管理员账号}  actionType  操作类型 增加 1 删除2 更新3
        Log log = new Log(null, 2, user.getName(), 3, "更新", "编辑终端账号：" + clientAccountVo.getAccount(), new Date(), businessUser.getBusinessId(), clientAccountVo.getClientId());
        boolean b = iLogService.addLog(log);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 重置密码
     *
     * @param clientId
     * @return
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    @RequiresPermissions("client:resetPassword")
    @AopLog(describe = "重置密码：", targetParamName = "userName", operationType = OperationType.RESET)
    public Object resetPassword(Integer clientId, @RequestParam(required = false) String userName) {
        BusinessUser currentUser = getCurrentUser();
        BusinessUser businessUser = businessUserService.findByBid(clientId.longValue());
        businessUser.setPassword(MD5Util.encrypt(businessUser.getUserName(), businessUser.getUserName()));

        businessUser.setModifyPerson(currentUser.getUserName());
        businessUser.setModifyTime(new Date());
        //添加日志
        try {
            businessUserService.addBusinessUser(businessUser);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        }

        String clientName = businessUserService.findByBid(clientId.longValue()).getUserName();
        //添加日志   accountType 账号类型{1:调度员,0:企业管理员;2:终端账号;3:其他管理员账号}  actionType  操作类型 增加 1 删除2 更新3
        Log log = new Log(null, 2, currentUser.getName(), 3, "更新", "重置终端账号：" + clientName + "的密码", new Date(), businessUser.getBusinessId(), clientId.longValue());
        boolean b = iLogService.addLog(log);
        /*ClientAccount clientAccount=clientAccountService.findByClientId(clientId);
        clientAccount.setPassword(MD5Util.encrypt(clientAccount.getAccount(),clientAccount.getAccount()));
        //添加日志
        clientAccountService.updateClientAccount(clientAccount);*/
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 改变终端账号的启动/禁用状态
     *
     * @param clientId
     * @param status
     * @return
     */
    @RequestMapping(value = "/changeStatus", method = RequestMethod.GET)
    @RequiresPermissions("client:changeStatus")
    @AopLog(describe = "更改终端的启用/禁用状态：", targetParamName = "userName", operationType = OperationType.UPDATE)
    public Object changeStatus(Integer clientId, Integer status, @RequestParam(required = false) String userName) {
        BusinessUser currentUser = getCurrentUser();
        if (status == 1) {
            //查询该企业下的终端数量是否超过限制数量
            if (!clientAccountService.isAllowChangeStatus(currentUser.getToken())) {
                return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "已超过终端账号数量限制");
            }
        }
        BusinessUser businessUser = new BusinessUser();
        businessUser.setBid(clientId.longValue());
        businessUser.setStatus(status);
        businessUser.setModifyPerson(currentUser.getUserName());
        businessUser.setModifyTime(new Date());
        try {
            businessUserService.addBusinessUser(businessUser);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        }

        String clientName = businessUserService.findByBid(clientId.longValue()).getUserName();
        //添加日志   accountType 账号类型{1:调度员,0:企业管理员;2:终端账号;3:其他管理员账号}  actionType  操作类型 增加 1 删除2 更新3
        Log log = new Log(null, 2, currentUser.getName(), 3, "更新", status == 0 ? "禁用终端账号：" + clientName : "启用终端账号：" + clientName, new Date(), businessUser.getBusinessId(), clientId.longValue());
        boolean b = iLogService.addLog(log);
        /*ClientAccount clientAccount=new ClientAccount();
        clientAccount.setClientId(clientId.longValue());
        clientAccount.setStatus(status);
        //添加日志
        clientAccountService.updateClientAccount(clientAccount);*/
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 移动时获取项目列表
     *
     * @param clientId
     * @return
     */
    @RequestMapping(value = "/getProjectList", method = RequestMethod.GET)
    public Object getProjectList(Integer clientId) {
        BusinessUser businessUser = businessUserService.findByBid(clientId.longValue());
        List<ProjectInfo> list = projectService.findMoveList(getCurrentUser().getBusinessId().intValue(), businessUser.getProjectId().intValue());

        /*ClientAccount clientAccount=clientAccountService.findByClientId(clientId);
        List<ProjectInfo> list=projectService.findMoveList(getCurrentUser().getBusinessId().intValue(),clientAccount.getProjectId().intValue());*/
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    /**
     * 移动终端账号到指定的项目
     *
     * @param projectId 项目ID
     * @param clientId  终端账号ID
     * @return
     */
    @RequestMapping(value = "/moveToTargetProject", method = RequestMethod.POST)
    @RequiresPermissions("client:move")
    public Object moveToTargetProject(Integer projectId, Long[] clientId) {
        ProjectInfo projectInfo = projectService.findByProjectId(projectId.longValue());
        if (projectInfo.getStatus() == 1) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "该项目已结束,不能移动账号到该项目");
        }
        BusinessUser currentUser = getCurrentUser();
        try {
            businessUserService.moveToTargetProject(projectId, clientId, currentUser);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 根据状态查询 账号启用状态{0:启用;1:禁用;2:全部}
     *
     * @return
     */
    @RequestMapping(value = "/findByStatus", method = RequestMethod.GET)
    public Object findByStatus(Integer projectId, Integer status, Integer pageSize, Integer pageNum) {
        PageInfo pageInfo = (status == null || status == 2) ? clientAccountService.findAllAccount(projectId, pageSize, pageNum) : clientAccountService.findByStatus(status, projectId, pageSize, pageNum);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 根据在线状态查询终端账号
     *
     * @param onlineStatus
     * @return
     */
    @RequestMapping(value = "/findByOnlineStatus", method = RequestMethod.GET)
    public Object findByOnlineStatus(Integer onlineStatus, Integer pageSize, Integer pageNum) {
        BusinessUser currentUser = getCurrentUser();
        PageInfo pageInfo = clientAccountService.findClientAccountByOnlineStatus(onlineStatus, currentUser.getBusinessId(), pageSize, pageNum);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 根据条件检索
     *
     * @param projectId
     * @param type
     * @param param
     * @return
     */
    @RequestMapping(value = "/findByParam", method = RequestMethod.GET)
    @RequiresPermissions(value = {"client:view"})
    public Object findByParam(Integer projectId, Integer type, String param, Integer pageSize, Integer pageNum) {
        PageInfo pageInfo = null;
        switch (type) {
            case 0:
                pageInfo = clientAccountService.findByName(projectId, param, pageSize, pageNum);//根据姓名查询
                break;
            case 1:
                pageInfo = clientAccountService.findByAccount(projectId, param, pageSize, pageNum);//根据账号
                break;
            case 2:
                pageInfo = clientAccountService.findByTel(projectId, param, pageSize, pageNum);//根据电话
                break;
            case 3:
                pageInfo = clientAccountService.findByNumber(projectId, param, pageSize, pageNum);//根据设备编码
                break;
            default:
                pageInfo = clientAccountService.findByName(projectId, param, pageSize, pageNum);//根据姓名查询
                break;
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 导出终端账号列表
     * create by wsm on 2019-11-28
     *
     * @param bids
     * @param response
     * @return
     */
    @PostMapping("/handleExportClientAccountList")
    @RequiresPermissions("client:export")
    public Object handleExportClientAccountList(Integer projectId, String bids, HttpServletResponse response) {
        logger.info("导出终端账号列表");
        JSONObject jsonObject = JSONObject.parseObject(bids);
        List<Long> list = jsonObject.getJSONArray("bid").toJavaList(Long.class);
        logger.info("导出终端列表" + list);
        if (list == null || list.size() == 0) {
            return new MyResponseEntity(505, "未选择导出的终端列表");
        }
        List<ClientAccountSelectVo> listC = clientAccountService.exportClientAccountExcelList(projectId, list);
        iExcelService.exportExcel(listC, ClientAccountSelectVo.class, "终端列表", response);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "ok");
    }


    /**
     * 总后台查询终端账号
     * create by wsm on 2019-12-3
     *
     * @param json
     * @return
     */
    @PostMapping("/handleSelectClientAccountByCondition")//@RequestBody参数接收json
    public String handleSelectClientAccountByCondition(@RequestBody String json) {
        logger.info("总后台查询终端账号" + json);
        //json 转对象
        MyClientAccountVo myClientAccountVo = JSON.parseObject(json, MyClientAccountVo.class);
        PageInfo myClientAccountVos = clientAccountService.selectClientAccountByCondition(myClientAccountVo);
        //集合转json
        String jsonString = JSON.toJSONString(myClientAccountVos);
        return jsonString;
    }


    /**
     * 总后台首页----查询统计终端 调度员  项目统计 详情
     *
     * @param json
     * @return
     */
    @PostMapping("/handleSelectClientCount")
    public String handleSelectClientCount(@RequestBody String json) {
        logger.info("总后台首页----查询统计终端 调度员  项目统计 详情" + json);
        //@RequestBody 代表接收的参数为json对象 必须解析为普通字符串才能被作为参数
        String s = JSON.parse(json).toString();
        List map = clientAccountService.selectCountByToken(s);
        logger.info("map===" + map);
        //查询到的数据必须转为json格式才能被请求端接收
        String jsonString = JSON.toJSONString(map);
        return jsonString;
    }


    /**
     * 总后台企业管理 -企业明细- 统计查询 通过地址查询
     * Statistic 统计
     *
     * @return
     */
    @PostMapping("/selectBusinessStatistics")
    public String selectBusinessStatistics(@RequestBody String json) {
        logger.info("总后台企业管理 -企业明细-统计查询-通过地址查询参数如下：" + json);
        JSONObject jsonObject = JSON.parseObject(json);
        Object address = jsonObject.get("address");
        Object token = jsonObject.get("token");
        Map map = clientAccountService.selectCountByAddress(token.toString(), address.toString());
        String jsonString = JSON.toJSONString(map);
        return jsonString;
    }

    /**
     * 终端app获取更新app的数据 ---向总后台获取
     *
     * @return
     */
    @PostMapping("/getClientAppUpdateData")
    public MyResponseEntity getClientAppUpdateData() {
        logger.info("终端app获取更新app的数据 ---向总后台获取");
        String s = HttpClientUtil.doPostJson("http://" + remoteUrl + "/appManager/getClientAppMessages", "");
        Map map = JSON.parseObject(s, Map.class);
//        map.put("apk_file_url", FilePathUtil.getBaseUrl(request)+map.get("apk_file_url").toString());
        logger.info("map==" + map);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), map);
    }


}
