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
import com.wyfx.business.entity.BusinessUser;
import com.wyfx.business.entity.Log;
import com.wyfx.business.entity.ProjectInfo;
import com.wyfx.business.entity.vo.DispatcherVo;
import com.wyfx.business.entity.vo.GpsVo;
import com.wyfx.business.entity.vo.MyDispatcherVo;
import com.wyfx.business.service.ProjectService;
import com.wyfx.business.service.TalkBackGroupMemberService;
import com.wyfx.business.service.TalkBackService;
import com.wyfx.business.service.common.ILogService;
import com.wyfx.business.service.shiro.BusinessUserService;
import com.wyfx.business.service.sys.ZidianServer;
import com.wyfx.business.service.trace.GpsService;
import com.wyfx.business.utils.AccountUtil;
import com.wyfx.business.utils.MD5Util;
import com.wyfx.business.utils.UserTypeAndStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author johnson liu
 * @date 2019/11/9
 * @description 调度员控制器
 */
@RestController
@RequestMapping("/dispatcher")
public class DispatcherController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherController.class);
    @Autowired
    private BusinessUserService businessUserService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private GpsService gpsService;
    @Autowired
    private ILogService iLogService;
    @Autowired
    private TalkBackService talkBackService;
    @Autowired
    private ZidianServer zidianServer;
    @Autowired
    private TalkBackGroupMemberService talkBackGroupMemberService;

    /**
     * 获取当前登录用户
     *
     * @return
     */
    @RequestMapping(value = "/getCurrentLoginUser", method = RequestMethod.GET)
    public Object getCurrentLoginUser() {
        BusinessUser user = getCurrentUser();
        String userName = user.getUserName();
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), userName);
    }

    /**
     * 注册调度员账号
     *
     * @param account
     * @param name
     * @param tel
     * @return
     */
    @RequestMapping(value = "/registerDispatcher", method = RequestMethod.POST)
//    @RequiresPermissions("dispatcher:add")
    @AopLog(describe = "创建调度员账号：", targetParamName = "account", operationType = OperationType.INSERT)
    public Object registerDispatcher(Integer projectId, String account, String pwd, String name, String tel) {
        if (!AccountUtil.checkAccountName(account) || name.length() > 20) {
            return new MyResponseEntity(ResponseCode.ERROR_PARAM.getValue(), "输入账号/姓名不符合要求");
        }
        //检查账号是否重复
        if (!businessUserService.checkUser(account)) {
            return new MyResponseEntity(ResponseCode.ERROR_PARAM.getValue(), "账号已存在");
        }
        if (!AccountUtil.vilicationPhone(tel)) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "电话号码格式错误");
        }
        ProjectInfo projectInfo = projectService.findByProjectId(projectId.longValue());
        if (projectInfo.getStatus() == 1) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "该项目已结束,不能创建账号");
        }
        BusinessUser currentUser = getCurrentUser();
        /*ZidianSetting zidianSetting= zidianServer.findByName(currentUser.getBusinessId(),"调度员");*/
        BusinessUser businessUser = new BusinessUser(null, account, MD5Util.encrypt(account, pwd), name, tel, currentUser.getUserName(), new Date(), null, null, null, UserTypeAndStatus.account_Enable_status,
                projectId.longValue(), currentUser.getToken(), currentUser.getBusinessId(), UserTypeAndStatus.DISPATCHER, 0, null);
        try {
            businessUserService.addBusinessUser(businessUser);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        }
        //添加日志   accountType 账号类型{1:调度员,0:企业管理员;2:终端账号;3:其他管理员账号}  actionType  操作类型 增加 1 删除2 更新3
        Log log = new Log(null, 1, currentUser.getName(), 1, "添加", "添加调度员账号：" + name, new Date(), businessUser.getBusinessId(), businessUser.getBid());
        boolean b = iLogService.addLog(log);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 编辑调度员信息
     *
     * @return
     */
    @RequestMapping(value = "/editDispatcher", method = RequestMethod.POST)
//    @RequiresPermissions("dispatcher:edit")
    @AopLog(describe = "编辑调度员账号：", targetParamName = "account", operationType = OperationType.INSERT)
    public Object editDispatcher(Integer bid, String account, String pwd, String name, String tel) {
        if (!AccountUtil.checkAccountName(account) || name.length() > 20) {
            return new MyResponseEntity(ResponseCode.ERROR_PARAM.getValue(), "输入账号/姓名不符合要求");
        }
        if (!AccountUtil.vilicationPhone(tel)) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "电话号码格式错误");
        }
        BusinessUser currentUser = getCurrentUser();
        BusinessUser businessUser = new BusinessUser(bid.longValue(), account, MD5Util.encrypt(account, pwd), name, tel, null, null, new Date(), currentUser.getUserName(), null, null, null, null, null, null, null, null);
        try {
            businessUserService.addBusinessUser(businessUser);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        }
        //添加日志信息
        //添加日志
        Log log = new Log(null, 1, currentUser.getName(), 3, "更新", "更新调度员账号：" + name, new Date(), businessUser.getBusinessId(), bid.longValue());
        boolean b = iLogService.addLog(log);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 更改调度员的启用/禁用状态
     *
     * @param bid
     * @param status
     * @return
     */
    @RequestMapping(value = "/changeDispatcherStatus", method = RequestMethod.GET)
//    @RequiresPermissions("dispatcher:changeDispatcherStatus")
    @AopLog(describe = "更改调度员的启用/禁用状态：", targetParamName = "userName", operationType = OperationType.UPDATE)
    public Object changeDispatcherStatus(Integer bid, Integer status, @RequestParam(required = false) String userName) {
        BusinessUser currentUser = getCurrentUser();
        BusinessUser businessUser = new BusinessUser();
        businessUser.setBid(bid.longValue());
        businessUser.setStatus(status);
        businessUser.setModifyPerson(currentUser.getUserName());
        businessUser.setModifyTime(new Date());
        try {
            businessUserService.addBusinessUser(businessUser);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        }

        String dispatcherName = businessUserService.findByBid(bid.longValue()).getUserName();
        //添加日志记录
        //添加日志
        Log log = new Log(null, 1, currentUser.getName(), 3, "更新", status == 0 ? "禁用调度员账号：" + dispatcherName : "启用调度员账号：" + dispatcherName, new Date(), businessUser.getBusinessId(), bid.longValue());
        boolean b = iLogService.addLog(log);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 重置密码
     *
     * @param bid
     * @return
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
//    @RequiresPermissions("dispatcher:resetPassword")
    @AopLog(describe = "重置密码：", targetParamName = "userName", operationType = OperationType.RESET)
    public Object resetPassword(Integer bid, @RequestParam(required = false) String userName) {
        BusinessUser currentUser = getCurrentUser();
        BusinessUser businessUser = businessUserService.findByBid(bid.longValue());
        businessUser.setPassword(MD5Util.encrypt(businessUser.getUserName(), businessUser.getUserName()));

        businessUser.setModifyPerson(currentUser.getUserName());
        businessUser.setModifyTime(new Date());

        String dispatcherName = businessUserService.findByBid(bid.longValue()).getUserName();
        //添加日志
        Log log = new Log(null, 1, currentUser.getName(), 3, "更新", "重置调度员账号：" + dispatcherName + "的密码", new Date(), businessUser.getBusinessId(), bid.longValue());
        boolean b = iLogService.addLog(log);
        try {
            businessUserService.addBusinessUser(businessUser);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 移动调度员账号到某一项目
     *
     * @param bid
     * @return
     */
    @RequestMapping(value = "/moveDispatcher", method = RequestMethod.POST)
//    @RequiresPermissions("dispatcher:move")
    public Object moveDispatcher(Long[] bid, Integer projectId) {
        ProjectInfo projectInfo = projectService.findByProjectId(projectId.longValue());
        if (projectInfo.getStatus() == 1) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "该项目已结束,不能移动账号到该项目");
        }
        BusinessUser currentUser = getCurrentUser();
        try {
            businessUserService.moveToTargetProject(projectId, bid, currentUser);
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        }

        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 获取可以移动的项目列表
     *
     * @param bid
     * @return
     */
    @RequestMapping(value = "/getProjectList", method = RequestMethod.GET)
    public Object getProjectList(Integer bid) {
        if (bid == null) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "未选中项目");
        }
        BusinessUser businessUser = businessUserService.findByBid(bid.longValue());
        List<ProjectInfo> list = projectService.findMoveList(getCurrentUser().getBusinessId().intValue(), businessUser.getProjectId().intValue());
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    /**
     * 查询某一项目下所有的调度员账号
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/findByAll", method = RequestMethod.GET)
    public Object findByAll(Integer projectId, Integer pageSize, Integer pageNum) {

        PageInfo<DispatcherVo> pageInfo = businessUserService.findByStatus(projectId, null, pageSize, pageNum);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 查询某一项目下某一状态的调度员账号
     *
     * @param projectId
     * @param status
     * @return
     */
    @RequestMapping(value = "/findByStatus", method = RequestMethod.GET)
    public Object findByStatus(Integer projectId, Integer status, Integer pageSize, Integer pageNum) {
        PageInfo<DispatcherVo> pageInfo = businessUserService.findByStatus(projectId, status, pageSize, pageNum);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 按条件查询某一项目下的调度员信息
     *
     * @param projectId
     * @param type
     * @param param
     * @return
     */
    @RequestMapping(value = "/findByParam", method = RequestMethod.GET)
//    @RequiresPermissions("dispatcher:view")
    public Object findByParam(Integer projectId, Integer type, String param, Integer pageSize, Integer pageNum) {
        PageInfo<DispatcherVo> pageInfo = businessUserService.findByParam(projectId, type, param, pageSize, pageNum);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 调度员上传定位信息
     *
     * @param longitude
     * @param latitude
     * @return
     */
    @RequestMapping(value = "/uploadLatLng", method = RequestMethod.POST)
    public Object uploadLatLng(Double longitude, Double latitude) {
        BusinessUser user = getCurrentUser();
        GpsVo gpsVo = new GpsVo();
        gpsVo.setLatitude(latitude);
        gpsVo.setLongitude(longitude);
        gpsService.addGpsInfo(gpsVo, user);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 企业总后台------查询调度员
     *
     * @param json
     * @return
     */
    @RequestMapping("/handleSelectDispatcherByCondition")
    public String handleSelectDispatcherByCondition(@RequestBody String json) {
        logger.info("企业总后台查询调度员");
        MyDispatcherVo myDispatcherVo = JSON.parseObject(json, MyDispatcherVo.class);
        PageInfo myDispatcherVos = businessUserService.selectDispatcherByCondition(myDispatcherVo);
        logger.info("调度员列表=" + myDispatcherVos);
        String jsonString = JSON.toJSONString(myDispatcherVos);
        return jsonString;
    }


    /**
     * 导出调度员列表
     * create by wsm on 2019-11-22
     *
     * @param bids
     * @param response
     * @return
     */
    @PostMapping("/handleExportDispatcher")
//    @RequiresPermissions("dispatcher:export")
    public Object handleExportDispatcher(Integer projectId, String bids, HttpServletResponse response) {
        logger.info("导出调度员列表");
        JSONObject jsonObject = JSONObject.parseObject(bids);
        List<Long> list = jsonObject.getJSONArray("bid").toJavaList(Long.class);
        logger.info("list===" + list);
        if (list == null || list.size() == 0) {
            return new MyResponseEntity(505, "未选择导出的调度员列表");
        }
        businessUserService.exportDispatcherExcel(projectId, list, response);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "导出调度员列表ok");
    }


    /**
     * 调度员app获取更新app的数据 ---向总后台获取
     *
     * @return
     */
    @PostMapping("/getDispatcherAppUpdateData")
    public MyResponseEntity getDispatcherAppUpdateData() {
        String s = HttpClientUtil.doPostJson("http://" + remoteUrl + "/appManager/getDispatcherAppMessages", "");
        Map map = JSON.parseObject(s, Map.class);
//        map.put("apk_file_url", FilePathUtil.getBaseUrl(request)+map.get("apk_file_url").toString());
        System.err.println("map==" + map);

        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), map);
    }


}
