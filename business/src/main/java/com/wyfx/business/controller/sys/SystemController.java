package com.wyfx.business.controller.sys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.alarmRange.util.HttpClientUtil;
import com.wyfx.business.controller.BaseController;
import com.wyfx.business.controller.commons.MyResponseEntity;
import com.wyfx.business.controller.commons.ResponseCode;
import com.wyfx.business.controller.commons.SafetyHatException;
import com.wyfx.business.controller.ws.WebSocketServer;
import com.wyfx.business.controller.ws.pojo.BaseCommand;
import com.wyfx.business.controller.ws.pojo.WsConstant;
import com.wyfx.business.entity.*;
import com.wyfx.business.entity.vo.DiskSpaceVo;
import com.wyfx.business.entity.vo.DiySetVo;
import com.wyfx.business.service.ClientAccountService;
import com.wyfx.business.service.common.*;
import com.wyfx.business.service.shiro.BusinessUserService;
import com.wyfx.business.service.sys.SystemServer;
import com.wyfx.business.utils.ConstantList;
import com.wyfx.business.utils.DateUtil;
import com.wyfx.business.utils.FilePathUtil;
import com.wyfx.business.utils.UserTypeAndStatus;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author johnson liu
 * @date 2019/12/12
 * @description 系统设置
 */
@RestController
@RequestMapping("/system")
public class SystemController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(SystemController.class);
    @Autowired
    private IBusinessMessagesCenterService iBusinessMessagesCenterService;
    @Autowired
    private SystemServer systemServer;
    @Autowired
    private BusinessUserService businessUserService;
    @Autowired
    private IBusinessInfoService iBusinessInfoService;
    @Autowired
    private ClientAccountService clientAccountService;
    @Autowired
    private ILogService iLogService;
    @Autowired
    private IExcelService iExcelService;
    @Autowired
    private IAppUpdateService iAppUpdateService;

    /**
     * 查询所有未读的消息详情
     * <p>
     * create by wsm on 2019-12-12
     *
     * @return
     */
    @GetMapping("/handleSelectAllMessages")
    public MyResponseEntity handleSelectAllMessages(Integer pageNum, Integer pageSize) {
        logger.info("查询某企业的所有未读消息");
        Map map = iBusinessMessagesCenterService.selectAllBusinessMessages(pageNum, pageSize);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), map);
    }

    /**
     * 更新消息阅读状态
     * create by wsm on 2019-12-12
     *
     * @param mid 需要更新状态的mid集合
     * @return
     */
    @PostMapping("/handleUpdateFlag")
    public MyResponseEntity handleUpdateFlag(String mid) {
        logger.info("更新已读消息状态mid集合=" + mid);
        List<Long> list = JSONArray.parseArray(mid).toJavaList(Long.class);
        boolean b = iBusinessMessagesCenterService.updateBusinessMessagesFlagByMid(list);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "修改成功");
    }

    /**
     * 查询是否存在未读消息
     * create by wsm on 2019-12-16
     *
     * @return 所有未读消息的id集合   无则返回空集
     */
    @GetMapping("/handleUnReadMessages")
    public MyResponseEntity handleUnReadMessages() {
        logger.info("查询是否存在未读消息");
        List<Long> list = iBusinessMessagesCenterService.selectAllUnReadMessages();
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), list);
    }

    /**
     * 系统设置----服务状态
     * create by wsm on 2019-12-12
     *
     * @return
     */
    @GetMapping("/serviceStatus")
    public MyResponseEntity serviceStatus() {
        logger.info("系统设置----服务状态");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String token = JSON.toJSONString(getCurrentUser().getToken());
        String s = HttpClientUtil.doPostJson("http://" + remoteUrl + "/enterprise/handleSelectBusinessManagerDiySetting", token);
        DiySetVo diySetVo = JSON.parseObject(s, DiySetVo.class);
        Map map = new HashMap((int) (3 / 0.75F) + 1);
        map.put("validityTime", sdf.format(diySetVo.getValidityTime()));//服务到期时间
        map.put("deviceNumLimit", diySetVo.getDeviceNumLimit()); //终端限制数量
        //查询当前终端数量
        Integer clientNum = clientAccountService.selectAllClientCountByToken(getCurrentUser().getToken());
        map.put("clientNum", clientNum);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), map);
    }

    @RequestMapping(value = "/getLogType", method = RequestMethod.GET)
    public MyResponseEntity getLogType() {
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), iLogService.getLogType());
    }

    /**
     * 通过条件检索日志记录----操作日志
     * create by wsm on 2019-12-21
     *
     * @param accountType
     * @param actionType
     * @param startTime
     * @param endTime
     * @param executor
     * @return
     */
    @PostMapping("/handleSelectLogByCondition")
//    @RequiresPermissions("log:view")
    public MyResponseEntity handleSelectLogByCondition(Integer accountType, Integer actionType, String startTime, String endTime, String executor, Integer pageNum, Integer pageSize) throws ParseException {
        logger.info("通过条件检索日志记录----操作日志" + accountType + "=" + actionType + "=");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PageInfo pageInfo = iLogService.selectByCondition(getCurrentUser().getBusinessId(), accountType, actionType,
                startTime == null || "".equals(startTime) ? null : sdf.parse(startTime),
                endTime == null || "".equals(endTime) ? null : sdf.parse(endTime), executor, pageNum, pageSize);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageInfo);
    }

    /**
     * 导出日志记录
     * create by wsm on 2019-12-21
     *
     * @param
     * @return
     */
    @PostMapping("/handleExportLog")
//    @RequiresPermissions("log:export")
    public MyResponseEntity handleExportLog(HttpServletResponse response) {
        logger.info("导出日志记录");
        List<Log> logs = iLogService.exportLog(getCurrentUser().getBusinessId());
        iExcelService.exportExcel(logs, Log.class, "操作日志", response);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 总后台远程控制---下载调度员或终端日志 通过bid查询
     *
     * @param bid
     * @return
     */
    @PostMapping("/handleExportLogByBid")
    public String handleExportLogByBid(@RequestBody String bid) {
        logger.info("总后台导出日志bid===" + bid);
        List<Log> logs = iLogService.exportLogByBid(Long.parseLong(JSON.parse(bid).toString()));
        String s = JSON.toJSONString(logs);
        return s;
    }

    /**
     * 总后台更新调度员app后马上推送消息个企业后台 企业后台马上推送更新app提示给所有调度员app
     *
     * @param json
     * @return
     */
    @PostMapping("/handlePushDispatcherAppUpdateMessages")
    @Deprecated
    public String handleDispatcherAppUpdatePush(@RequestBody String json) {
        //  查询所有在线调度员

        //  推送更新提示到调度员app  直接推送json数据给调度员app 无需存库
        System.err.println("调度员app更新提示  ====" + json);

//        if (b) {
//            return "true";
//        }
        return "false";
    }

    /**
     * 总后台更新终端app时 只向企业后台插入版本信息 其他推送给终端app的数据需要在推送时主动去总后台查询
     * 自动更新app
     *
     * @param json
     * @return
     */
    @PostMapping("/handlePushClientVersionMessages")
    public String handlePushClientVersionMessages(@RequestBody String json) {
        logger.info("总后台更新终端app时 只向企业后台插入版本信息");
        // 更新终端app数据库版本信息 id默认为1
        String s = JSON.parse(json).toString();
        //查询是否是第一次添加数据
        AppUpdateSetting appSetById = iAppUpdateService.findAppSetById(1L);
        AppUpdateSetting appset;
        boolean b;
        if (appSetById == null) {//第一次插入版本数据 默认为自动更新app
            appset = new AppUpdateSetting(1L, s, s, 1);
            b = iAppUpdateService.addAppUpdateSet(appset);
        } else {//更新版本数据
            //查询app更新版本设置 如果设置为自动使用平台最新版( 更新设置 1自动使用平台最新版 2手动更新)则立即推送给所有在线终端
            if (appSetById.getUpdateSet() == 1) {
                // todo 发送命令提示终端app有新版本 需要测试
                WebSocketServer.sendAllMessage(JSON.toJSONString(new BaseCommand(WsConstant.updateApp.name(), "", "client app have new version")));
                appset = new AppUpdateSetting(1L, s, s, null);
            } else {//需要手动更新数据库和推送消息给终端 todo  调用此接口通知终端 /handleUpdateManuallyApp
                appset = new AppUpdateSetting(1L, appSetById.getLastVersion(), s, null);
            }
            b = iAppUpdateService.editAppUpdateSet(appset);
        }
        if (b) {
            return "true";
        }
        return "false";
    }

    /**
     * 企业后台-----app版本更新设置为手动或自动更新
     * 逻辑 所有的app都是在总后台发布后才推送到app终端 所以第一次app更新设置的数据由总后台插入
     *
     * @param updateSet
     * @return
     */
    @PostMapping("/handleUpdateAppVersionSet")
//    @RequiresPermissions("app:save")
    public MyResponseEntity handleAddAppUpdateSet(Integer updateSet) {
        logger.info("企业后台-----app版本更新设置为手动或自动更新");
        AppUpdateSetting appset = new AppUpdateSetting(1L, null, null, updateSet);
        boolean b = iAppUpdateService.editAppUpdateSet(appset);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 刷新页面 查询 app更新设置和版本数据
     *
     * @return
     */
    @GetMapping("/handleSelectAppSet")
    public MyResponseEntity handleSelectAppSet() {
        logger.info("刷新页面 查询 app更新设置和版本数据");
        AppUpdateSetting appSetById = iAppUpdateService.findAppSetById(1L);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), appSetById);
    }

    /**
     * 企业后台---根据终端app更新设置 手动推送app更新提示给终端app 获取总后台需要推送的数据格式进行推送
     * 手动更新 app
     */
    @GetMapping("/handleUpdateManuallyApp")
//    @RequiresPermissions("app:update")
    public MyResponseEntity handleUpdateManuallyApp() {
        logger.info("企业后台---根据终端app更新设置 手动推送app更新提示给终端app 获取总后台需要推送的数据格式进行推送");
        // 更新版本号
        AppUpdateSetting appSetById = iAppUpdateService.findAppSetById(1L);
        appSetById.setCurrentVersion(appSetById.getLastVersion());
        appSetById.setUpdateSet(2);//1 自动 2手动
        boolean b = iAppUpdateService.editAppUpdateSet(appSetById);
        // 查询需要推送的数据
//        String s = HttpClientUtil.doPostJson("http://" + remoteUrl + "/appManager/getClientAppPushMessages", "");
//        System.err.println("推送数据============" + s);
        //todo 发送命令提示终端app有新版本 需要测试
        WebSocketServer.sendAllMessage(JSON.toJSONString(new BaseCommand(WsConstant.updateApp.name(), "", "client app have new version")));
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "成功推送app更新提示到终端");
    }


    /**
     * 总后台---切换终端连接方式  用于切换服务器使用
     *
     * @param params
     * @return
     */
    @PostMapping("/handleChangeClientConnectionMethod")
    public String handleChangeClientConnectionMethod(@RequestBody Map<String, String> params) {
        logger.info("总后台---切换终端连接方式  用于切换服务器使用");
        int option = Integer.parseInt(params.get("option"));
        if (option == 1) {//1 ip更新 2 域名更新
            String ip = params.get("ip");
            String port = params.get("port");
            logger.info("ip端口更新==" + ip + ":" + port);
            // 发送命令切换服务器 需测试
            WebSocketServer.sendAllMessage(JSON.toJSONString(new BaseCommand(WsConstant.SwitchServer.name(), "0", ip + ":" + port)));
        } else if (option == 2) {
            String domainName = params.get("domainName");
            logger.info("域名更新==" + domainName);
            // 发送命令切换服务器 需测试
            WebSocketServer.sendAllMessage(JSON.toJSONString(new BaseCommand(WsConstant.SwitchServer.name(), "1", domainName)));
        }
        //切换成功返回"true" 否则 返回 "false"
        return "true";
    }


    /**
     * 总后台---查询企业存储空间
     *
     * @param token
     * @return
     */
    @PostMapping("/handleQueryStropTip")
    public Object handleQueryStropTip(@RequestBody String token) {
        // todo 通过token查询主账号
        logger.info("总后台---查询企业存储空间token==" + token);
        JSONObject jsonObject = JSON.parseObject(token);
        String token1 = jsonObject.getString("token");
        String mainAccount = iBusinessInfoService.findByToken(token1).getMainAccount();
        //  查询企业存储空间
        DiskSpaceVo diskSpaceVo = new DiskSpaceVo();
        try {
            String mainAccountPath = FilePathUtil.getExcuteJarPath() + File.separator + "safety-hat" + File.separator + mainAccount;
            File mainAccountFile = new File(mainAccountPath);
            if (mainAccountFile.exists()) {
                diskSpaceVo.setUsed(FileUtils.sizeOfDirectory(mainAccountFile));
            }
            File[] files = mainAccountFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    switch (file.getName()) {
                        case UserTypeAndStatus.imagesPath:
                            diskSpaceVo.setImages(FileUtils.sizeOfDirectory(file));
                            break;
                        case UserTypeAndStatus.videoPath:
                            diskSpaceVo.setVideo(FileUtils.sizeOfDirectory(file));
                            break;
                        case UserTypeAndStatus.logPath:
                            diskSpaceVo.setLog(FileUtils.sizeOfDirectory(file));
                            break;
                        case UserTypeAndStatus.talkBackPath:
                            diskSpaceVo.setTalkBack(FileUtils.sizeOfDirectory(file));
                            break;
                        case UserTypeAndStatus.phoneRecordPath:
                            diskSpaceVo.setPhoneRecord(FileUtils.sizeOfDirectory(file));
                            break;
                        case UserTypeAndStatus.broadcastPath:
                            diskSpaceVo.setBroadcast(FileUtils.sizeOfDirectory(file));
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), diskSpaceVo);
    }


    /**
     * 总后台---删除企业存储空间
     * options 选项 1照片 2视频 3日志 4对讲记录 5电话录音  6广播记录语音
     * days   清理的天数
     * mainAccount 主账号
     *
     * @param params
     * @return
     */
    @PostMapping("/handleDeleteStropTip")
    public String handleDeleteStropTip(@RequestBody Map<String, Object> params) {
        logger.info("总后台---删除企业存储空间");
        String token = params.get("token").toString();
        // 通过token查询主账号
        String mainAccount = iBusinessInfoService.findByToken(token).getMainAccount();
        List<Integer> options = (List<Integer>) params.get("options");
        List<String> folders = new ArrayList<>(options.size());
        for (int i = 0; i < options.size(); i++) {
            switch (options.get(i)) {
                case 1:
                    folders.add(UserTypeAndStatus.imagesPath);
                    break;
                case 2:
                    folders.add(UserTypeAndStatus.videoPath);
                    break;
                case 3:
                    folders.add(UserTypeAndStatus.logPath);
                    break;
                case 4:
                    folders.add(UserTypeAndStatus.talkBackPath);
                    break;
                case 5:
                    folders.add(UserTypeAndStatus.phoneRecordPath);
                    break;
                case 6:
                    folders.add(UserTypeAndStatus.broadcastPath);
                    break;
            }
        }
        String days = params.get("days").toString();
        // 执行删除
        String appointDate = DateUtil.getAppointDate(Integer.parseInt(days));
        String mainAccountPath = FilePathUtil.getExcuteJarPath() + File.separator + "safety-hat" + File.separator + mainAccount;
        try {
            File mainAccountFile = new File(mainAccountPath);
            if (!mainAccountFile.exists()) {
                return "true";
            }
            File[] files = mainAccountFile.listFiles();
            String fileName = null;
            for (File file : files) {
                fileName = file.getName();
                if (folders.contains(fileName)) {
                    File[] dateFiles = file.listFiles();
                    for (int i = 0; dateFiles != null && i < dateFiles.length; i++) {
                        if (!DateUtil.compareDate(dateFiles[i].getName(), appointDate)) {
                            FilePathUtil.deleteFile(dateFiles[i]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            return "false";
        }
        //成功返回"true"否则返回"false"
        return "true";
    }


    /**
     * 添加或修改系统页面配置
     *
     * @param name
     * @param logoImg
     * @param metaImg
     * @return
     */
    @RequestMapping(value = "/page/addPageSetting", method = RequestMethod.POST)
//    @RequiresPermissions("page_setting:save")
    public Object addPageSetting(String name, MultipartFile logoImg, MultipartFile metaImg) {
        BusinessUser businessUser = getCurrentUser();
        systemServer.addLogoImg(name, logoImg, metaImg, businessUser);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }

    /**
     * 获取页面配置信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/page/findPageSetting", method = RequestMethod.GET)
    public Object findPageSetting(HttpServletRequest request) {
        BusinessUser businessUser = getCurrentUser();
        BusinessPageSetting pageSetting = systemServer.findPageSettingByBusinessId(businessUser.getBusinessId(), request);
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), pageSetting);
    }

    /**
     * 获取视频分辨率类型列表
     *
     * @return
     */
    @GetMapping(value = "/video/getResolutionType")
    public Object getResolutionType() {
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), systemServer.getResolutionType());
    }

    public Object synchronizeVideoParam2() {
        try {
            BusinessUser user = getCurrentUser();
            DefaultClientVideo defaultClientVideo = systemServer.findByBusinessId(user.getBusinessId());
            Integer resolution = (defaultClientVideo == null) ? null : defaultClientVideo.getResolution();
            systemServer.synchronizeVideoParam(user.getBusinessId(), resolution);
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
     * 添加终端视频参数设置
     *
     * @param type    视频分辨率类型
     * @param bitRate 视频码率
     * @return
     */
    @RequestMapping(value = "/video/addVideoParamSetting", method = RequestMethod.POST)
//    @RequiresPermissions("controller:addVideoParamSetting")
    public Object addVideoParamSetting(Integer type, Integer bitRate) {
        BusinessUser businessUser = getCurrentUser();
        systemServer.addVideoParamSetting(type, bitRate, businessUser);

        synchronizeVideoParam2();
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }


//    /**
//     * 获取终端视频参数
//     * @return
//     */
//    @RequestMapping(value = "/video/getVideoParamSetting", method = RequestMethod.GET)
//    @RequiresPermissions("controller:addVideoParamSetting")
//    public Object addVideoParamSetting() {
//        BusinessUser businessUser = getCurrentUser();
//        systemServer.addVideoParamSetting(type, bitRate, businessUser);
//        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
//    }

    /**
     * 查询该企业的视频参数设置
     *
     * @return
     */
    @RequestMapping(value = "/video/findVideoParamSetting", method = RequestMethod.GET)
    public Object findVideoParamSetting() {
        BusinessUser businessUser = getCurrentUser();
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), systemServer.findByBusinessId(businessUser.getBusinessId()));
    }

    /**
     * 销毁终端数据
     *
     * @param clientId 终端账号
     * @param type     {1:"图片";2:"视频";3:"日志"}
     * @return
     */
    @RequestMapping(value = "/controller/destroyClientData", method = RequestMethod.POST)
//    @RequiresPermissions("controller:destroyClientData")
    public Object destroyClientData(Long clientId, String type) {
        try {
            String[] strArray = type.split("[^\\w\\d]");
            List<Integer> list = new ArrayList<>();
            for (String str : strArray) {
                if (str != null && !str.isEmpty()) {
                    list.add(Integer.valueOf(str));
                }
            }
            Integer[] types = new Integer[list.size()];
            systemServer.destroyClientData(clientId, list.toArray(types));
        } catch (SafetyHatException e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "操作失败");
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "销毁命令发送成功");
    }

    /**
     * 变更终端登录账号
     *
     * @param beforeClientId 当前登录账号
     * @param afterClientId  待替换的终端账号
     * @return
     */
    @RequestMapping(value = "/controller/changeClientAccount", method = RequestMethod.POST)
//    @RequiresPermissions("controller:changeClientAccount")
    public Object changeClientAccount(Long beforeClientId, Long afterClientId) {
        try {
            systemServer.changeClientAccount(beforeClientId, afterClientId);
        } catch (SafetyHatException e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), e.getMessage());
        } catch (Exception e) {
            return new MyResponseEntity(ResponseCode.ERROR_SYS.getValue(), "操作失败");
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue(), "发送变更终端账号命令成功");
    }

    /**
     * 同步视频参数
     *
     * @return
     */


    @RequestMapping(value = "/controller/synchronizeVideoParam", method = RequestMethod.POST)
//    @RequiresPermissions("controller:changeClientAccount")
    public Object synchronizeVideoParam() {
        try {
            BusinessUser user = getCurrentUser();
            DefaultClientVideo defaultClientVideo = systemServer.findByBusinessId(user.getBusinessId());
            Integer resolution = (defaultClientVideo == null) ? null : defaultClientVideo.getResolution();
            systemServer.synchronizeVideoParam(user.getBusinessId(), resolution);
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
     * 总后台修改配置,并通知在线设备
     *
     * @param diySetVo 所有的配置信息
     * @return
     */
    @RequestMapping(value = "/updateBusinessOfSetting", method = RequestMethod.POST)
    public Object updateBusinessOfSetting(@RequestBody DiySetVo diySetVo) {
        logger.info("总后台修改配置,并通知在线设备");
        try {
            WebSocketServer.sendAllMessage(JSONObject.toJSONString(ConstantList.buildBaseCommand(diySetVo, WsConstant.config.name(), "")));//给所有在线用户发送修改后的配置消息
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MyResponseEntity(ResponseCode.SUCCESS.getValue());
    }


}
