package com.wyfx.total.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wyfx.total.entity.BusinessManager;
import com.wyfx.total.entity.DefaultSetting;
import com.wyfx.total.entity.DiySetting;
import com.wyfx.total.entity.vo.BusinessInfo;
import com.wyfx.total.entity.vo.DiySetVo;
import com.wyfx.total.entity.vo.MyResponseEntity;
import com.wyfx.total.exception.DateFormatException;
import com.wyfx.total.exception.NotHaveDefaultSettingException;
import com.wyfx.total.service.AreasService;
import com.wyfx.total.service.EnterpriseManagementService;
import com.wyfx.total.service.IDefaultSettingService;
import com.wyfx.total.service.ILogInfService;
import com.wyfx.total.service.impl.DlbsAuthorizationManagementImp;
import com.wyfx.total.utile.HttpClientUtil;
import com.wyfx.total.utile.RespBean;
import com.wyfx.total.utile.ResponseCode;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName: EnterpriseManagementController
 * @Description: 企业管理
 * @author： 王世明
 * @date 2019-11-2
 */
@RestController
@RequestMapping(value = "/enterprise")//先继承再实现
@Api(tags = "企业管理")
public class EnterpriseManagementController extends BaseController {
    protected static Logger logger = LoggerFactory.getLogger(EnterpriseManagementController.class);
    @Autowired
    private EnterpriseManagementService enterpriseManagementService;
    @Autowired
    private ILogInfService iLogInfService;
    /**
     * 省市联动
     */
    @Autowired
    private AreasService areasService;


    /**
     * 默认设置
     */
    @Autowired
    private IDefaultSettingService iDefaultSettingService;
    @Autowired
    private DlbsAuthorizationManagementImp dlbsAuthorizationManagementImp;

    /**
     * 添加企业
     *
     * @param businessManager 企业管理
     * @param diySetting      自定义设置
     * @return
     */
    @PostMapping(value = "/addEnterprise")
    @ApiOperation(value = "添加企业", httpMethod = "POST", produces = "form-data")
    @ApiResponses({
            @ApiResponse(code = 200, message = "添加成功"),
            @ApiResponse(code = 211, message = "没有添加系统默认设置"),
            @ApiResponse(code = 502, message = "向企业后台插入数据失败，联系管理员"),
            @ApiResponse(code = 501, message = "手机或坐机号码格式错误"),
            @ApiResponse(code = 201, message = "添加失败"),
            @ApiResponse(code = 202, message = "企业名冲突"),
            @ApiResponse(code = 215, message = "企业名长度限制在20个字内"),
            @ApiResponse(code = 204, message = "主账号格式错误"),
    })
    public Object addEnterprise(@ApiParam BusinessManager businessManager, @ApiParam DiySetting diySetting) {
        logger.info("添加企业" + businessManager);
        logger.info("自定义设置" + diySetting);
        //生成企业uid
        String uuid = getUUID();
        //设置企业bid
        businessManager.setBid(uuid);
        //自定义设置中设置企业bid
        diySetting.setBid(uuid);
        //设置主账号密码
        businessManager.setPassword(businessManager.getMainAccount());
        String mobilePhoneRegex = "^(1[3-9])\\d{9}$";
        String phoneRegex = "^0[0-9]{2,3}-[0-9]{8}";
        String phone = businessManager.getPicPhone();
        //验证座机号码
        if (phone.contains("-")) {
            logger.info("-------------------------");
            boolean matches = phone.matches(phoneRegex);
            if (!matches) {
                return new RespBean(501, "座机号码格式错误");
            }
        } else {
            boolean matches = phone.matches(mobilePhoneRegex);
            if (!matches) {
                return new RespBean(501, "手机号码格式错误");
            }
        }

        //查询是否存在系统默认设置
        DefaultSetting defaultSetting = iDefaultSettingService.findDefaultSetting();
        if (defaultSetting == null) {
            throw new NotHaveDefaultSettingException("没有添加系统默认设置");
        }

        Map map = new HashMap();
        if (enterpriseManagementService.addEnterprise(businessManager, diySetting)) {
            map.put("message", "添加成功");
            //添加成功则向企业后台插入数据
            logger.info("向企业后台插入的企业数据=" + businessManager);
            BusinessInfo businessInfo = new BusinessInfo(null,
                    businessManager.getBname(), businessManager.getBid(), businessManager.getMainAccount(),
                    businessManager.getDeviceNumLimit(), businessManager.getStatus(), 0);
            String jsonString = JSON.toJSONString(businessInfo);
            String url = "http://" + businessRemoteUrl + "/user/handleAddBInfoAndBUser";
            String s = HttpClientUtil.doPostJson(url, jsonString);
            logger.info("向企业后台插入的数据返回结果=" + s);
            if ("".equals(s) || "false".equals(s)) {
                return new RespBean(502, "向企业后台插入数据失败，联系管理员");
            }
            // 新增企业日志记录
            iLogInfService.addLogInfRecord("添加企业", 0, businessManager.getBname());
            return new RespBean(ResponseCode.SUCCESS_CODE, map);
        } else {
            map.put("message", "添加失败");
            return new RespBean(201, map);
        }
    }

    /**
     * 查询账号是否重复
     *
     * @param mainAccount
     * @return
     */
    @GetMapping("/handleQueryAccount")
    public RespBean handleQueryAccount(String mainAccount) {
        BusinessManager businessManager = enterpriseManagementService.selectBusinessByMainAccount(mainAccount);
        if (businessManager == null) {
            return new RespBean(200, "企业不存在可以添加");
        }
        return new RespBean(203, "主账号冲突");
    }

    /**
     * 功能：编辑企业详情
     *
     * @param businessManager
     * @param diySetting
     * @return
     */
    @PostMapping("/editEnterpriseDetail")
    @ApiOperation(value = "编辑企业", httpMethod = "POST", produces = "form-data")
    @ApiResponses({
            @ApiResponse(code = 200, message = "添加成功"),
            @ApiResponse(code = 502, message = "向企业后台插入数据失败，联系管理员"),
            @ApiResponse(code = 501, message = "手机或坐机号码格式错误"),
            @ApiResponse(code = 201, message = "添加失败"),
    })
    public RespBean editEnterpriseDetail(@ApiParam BusinessManager businessManager, @ApiParam DiySetting diySetting) throws Exception {

        logger.info("编辑企业详情=businessManager=========" + businessManager);
        logger.info("diySetting==========" + diySetting);

//        把所有跟随系统的配置转化为系统配置的实际值
        DefaultSetting defaultSetting = iDefaultSettingService.findDefaultSetting();
        Class<DiySetting> diySettingClass = DiySetting.class;
        Field[] declaredFields = diySettingClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            Object o = declaredField.get(diySetting);
            if (o.equals(-2)) {
                String fieldName = declaredField.getName();
                Field field = DefaultSetting.class.getDeclaredField(fieldName);
                if (field!=null) {
                    declaredField.set(diySetting, field.get(defaultSetting));
                }
            }
        }
        String mobilePhoneRegex = "^(1[3-9])\\d{9}$";
        String phoneRegex = "^0[0-9]{2,3}-[0-9]{8}";
        String phone = businessManager.getPicPhone();
        //验证座机号码
        if (phone.contains("-")) {
            logger.info("验证座机号----------------------------------------------------");
            boolean matches = phone.matches(phoneRegex);
            if (!matches) {
                return new RespBean(501, "座机号码格式错误");
            }
        } else {
            boolean matches = phone.matches(mobilePhoneRegex);
            if (!matches) {
                return new RespBean(501, "手机号码格式错误");
            }
        }
        Map map = new HashMap();
        //判断主账号合法性 3-10位字母数字字符
        boolean b1 = enterpriseManagementService.updateEnterprise(businessManager, diySetting);
        if (b1) {
            map.put("message", "更新成功");
            logger.info("向企业后台更新数据=" + businessManager);
            BusinessInfo businessInfo = new BusinessInfo(
                    null,
                    businessManager.getBname(), businessManager.getBid(), businessManager.getMainAccount(),
                    businessManager.getDeviceNumLimit(), businessManager.getStatus(), 0);
            String jsonString = JSON.toJSONString(businessInfo);
            String url = "http://" + businessRemoteUrl + "/user/handleUpdateBInfoAndBUser";
            String s = HttpClientUtil.doPostJson(url, jsonString);
            //将企业的更新后的自定义设置查询出来推送给企业后台
            DiySetVo diySetVo = enterpriseManagementService.selectBusinessDiySettingByBid(businessManager.getBid());
            // 更新自定义设置时推送给企业后台
            String json = JSON.toJSONString(diySetVo);
            String s1 = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/system/updateBusinessOfSetting", json);
            boolean b2 = ("".equals(s) || "false".equals(s)) && ("".equals(s1) || "false".equals(s1));
            if (b2) {
                return new RespBean(502, "向企业后台插入数据失败，联系管理员");
            }
            // 更新企业日志记录
            iLogInfService.addLogInfRecord("更新企业", 2, businessManager.getBname());
            return new RespBean(ResponseCode.SUCCESS_CODE, map);
        } else {
            map.put("message", "更新失败");
            return new RespBean(202, map);
        }
    }

    /**
     * 重置企业密码
     *
     * @param bid
     * @return
     */
    @GetMapping(value = "/restPwd")
    @ApiOperation(value = "重置企业密码", httpMethod = "GET", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bid", value = "企业id", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "重置成功"),
            @ApiResponse(code = 500, message = "重置失败联系管理员"),
            @ApiResponse(code = 201, message = "企业ID不能为空"),
    })
    public RespBean handleRestPassWord(String bid) {
        logger.info("重置企业密码");
        Map map = new HashMap();
        if (bid == null || "".equals(bid)) {
            map.put("messages", "企业ID不能为空");
            return new RespBean(201, map);
        } else {
            boolean b = enterpriseManagementService.restPassWord(bid);
            if (b) {
                map.put("messages", "重置成功");
                //修改企业后台数据
                Map map1 = enterpriseManagementService.selectEnterpriseDetail(bid);
                String mainAccount = ((BusinessManager) map1.get("businessManager")).getMainAccount();
                Map jsonMap = new HashMap();
                jsonMap.put("mainAccount", mainAccount);//主账号即初始命密码
                jsonMap.put("token", bid);
                String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/user/handleRestPassWord", JSON.toJSONString(jsonMap));
                if ("".equals(s) || s == null) {
                    map.put("messages", "重置企业后台数据失败，联系管理员");
                }
                return new RespBean(ResponseCode.SUCCESS_CODE, map);
            } else {
                map.put("messages", "重置失败联系管理员");
                return new RespBean(500, map);
            }
        }
    }

    /**
     * 修改企业的合作状态-暂停合作
     *
     * @param bid 企业的bid
     * @return
     */
    @PostMapping("/pauseOperation")
    @ApiOperation(value = "暂停合作企业", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bid", value = "企业id", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "暂停企业成功"),
            @ApiResponse(code = 201, message = "暂停企业失败"),
    })
    public RespBean handlePauseStateOperation(String bid) {
        logger.info("暂停企业的bid" + bid);
        boolean b = enterpriseManagementService.pauseCooperationState(bid);
        Map map = new HashMap();
        if (b) {
            map.put("messages", "修改成功");
            //修改企业后台数据
            String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/user/handlePauseOperation", JSON.toJSONString(bid));
            if ("".equals(s) || s == null) {
                map.put("messages", "修改企业后台数据失败，联系管理员");
            }
            return new RespBean(ResponseCode.SUCCESS_CODE, map);
        }
        map.put("messages", "修改失败，联系管理员");
        return new RespBean(201, map);
    }

    /**
     * 重启合作企业
     *
     * @param bid     企业的bid
     * @param endTime 合作到期时间
     * @return
     */
    @PostMapping("/restartOperation")
    @ApiOperation(value = "重启合作企业", httpMethod = "POST", produces = "form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bid", value = "企业id", required = true),
            @ApiImplicitParam(name = "endTime", value = "到期时间", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "重启企业成功"),
            @ApiResponse(code = 201, message = "重启企业失败"),
            @ApiResponse(code = 206, message = "输入时间不合理,必须为当前时间以后"),
    })
    public RespBean handleRestartStateOperation(String bid, String endTime) throws ParseException {
        logger.info("重启企业" + bid);
        Date end = sdf.parse(endTime);
        Date now = new Date();
        if (end.getTime() < now.getTime()) {
            throw new DateFormatException("输入时间不合理,必须为当前时间以后。");
        }
        boolean b = enterpriseManagementService.restartCooperationState(bid, end);
        if (b) {
            //修改企业后台数据
            String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/user/handleRestartOperation", JSON.toJSONString(bid));
            if ("".equals(s) || s == null) {
                return new RespBean(201, "重启失败企业后台数据失败");
            }
            return new RespBean(ResponseCode.SUCCESS_CODE, "重启成功");
        }
        return new RespBean(201, "重启失败");

    }

    /**
     * 查询所有的企业
     *
     * @param pageNum  当前页面数
     * @param pageSize 每页数据量
     * @return
     */
    @PostMapping("/selectAllBusinessManager")
    public RespBean selectEnterpriseList(String pageNum, String pageSize) {
        logger.info("查询所有的企业selectEnterpriseList");
        Map allBusinessManager = enterpriseManagementService.findAllBusinessManager(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        return new RespBean(ResponseCode.SUCCESS_CODE, allBusinessManager);
    }

    /**
     * 功能：企业管理 按条件进行筛选查询
     * 用对象接受数据是注意当前端不提供值时一般会传“” 而不是NUll过来
     *
     * @param businessManager
     * @return
     */
    @PostMapping(value = "/selectEnterprise")
    public Object selectEnterprise(BusinessManager businessManager, String startTime, String endTime, Integer pageNum, Integer pageSize) throws ParseException {
        logger.info("selectEnterprise(1)按条件进行筛选查询==" + businessManager);
        //时间格式精确到天 也可以精确到秒
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start;
        if (startTime != null && !"".equals(startTime)) {
            start = sdf.parse(startTime);
        } else {
            start = null;
        }
        Date end;
        if (endTime != null && !"".equals(endTime)) {
            end = sdf.parse(endTime);
        } else {
            end = null;
        }
        //转换“”为null //可以在mapper.xml文件中的if标签中限制
        BusinessManager b = new BusinessManager();
        b.setStatus(businessManager.getStatus());
        b.setMainAccount("".equals(businessManager.getMainAccount()) ? null : businessManager.getMainAccount());
        b.setAddress("".equals(businessManager.getAddress()) ? null : businessManager.getAddress());
        b.setBname("".equals(businessManager.getBname()) ? null : businessManager.getBname());
        Map map = enterpriseManagementService.selectEnterpriseByCondition(b, start, end, pageNum, pageSize);
        logger.info("转换后的对象=" + b);
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }

    /**
     * 获取省市区列表
     * 未用到
     *
     * @param parentId
     * @return
     */
    @Deprecated
    @PostMapping("/getSubList")
    public RespBean handleGetSubList(String parentId) {
        Integer pid = Integer.parseInt(parentId);
        Map map = areasService.getSubList(pid);
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }

    /**
     * 功能：企业管理详情查看
     *
     * @param
     * @return
     */
    @PostMapping(value = "/selectEnterpriseDetail")
    public Object selectEnterpriseDetail(String bid) {
        logger.info("企业管理详情查看" + bid);
        if (null == bid || "".equals(bid)) {
            return new RespBean(205, "企业ID 不能为空");
        }
        Map map = enterpriseManagementService.selectEnterpriseDetail(bid);
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }

    /**
     * 企业管理 -企业明细- 统计查询
     * Statistic 统计
     * 暂停使用 切换至企业后台查询数据
     *
     * @param address 用户给定的地址范围
     * @param bid     企业bid
     * @return
     */
    @PostMapping("/selectEnterpriseStatistic")
    @Deprecated
    public RespBean selectEnterpriseStatistics(String address, String bid) {
        logger.info("企业管理 -企业明细- 统计查询" + bid + "address" + address);
        Map map = enterpriseManagementService.selectEnterpriseStatisticalByAddressAndBid(address, bid);
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }

    /**
     * 首页--通过合作状态查询所有的企业信息
     *
     * @param status 状态 0合作中 暂停1  全部null
     * @param bname  企业名字
     * @return
     */
    @PostMapping("/handleSelectAllBusinessManagerByState")
    public RespBean handleSelectAllBusinessManagerByState(Integer status, Integer pageNum, Integer pageSize, String bname) {
        logger.info("通过合作状态查询所有的企业信息");
        Map map = enterpriseManagementService.selectAllBusinessManagerByState(status, pageNum, pageSize, bname);
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }

    /**
     * 首页------- 查询所有企业的坐标
     * 通过合作状态查询 合作状态{0:合作中;1:暂停合作 }
     *
     * @return
     */
    @GetMapping("/handleSelectAllBusinessCoordinate")
    public RespBean handleSelectAllBusinessCoordinate(Integer status) {
        List list = enterpriseManagementService.selectAllBusinessCoordinate(status);
        return new RespBean(ResponseCode.SUCCESS_CODE, list);
    }

    /**
     * 首页 --通过id进行企业统计查询(项目 终端 调度员)
     * 暂停使用 切换至企业后台查询数据
     *
     * @param bid
     * @return
     */
    @Deprecated
    @GetMapping("/handleSelectEnterpriseStatisticsById")
    public RespBean handleSelectEnterpriseStatisticsById(String bid) {
        List map = enterpriseManagementService.selectEnterpriseStatisticalByBid(bid);
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }

    /**
     * 首页--通过名字模糊查询
     * 暂时未用到
     *
     * @param bname
     * @return
     */
    @Deprecated
    @GetMapping("/handleSelectBusinessManagerByBname")
    public RespBean handleSelectBusinessManagerByBname(String bname) {
        Map map = enterpriseManagementService.selectBusinessByName(bname);
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }

    /**
     * 企业后台-----请求查看总后台企业的自定义设置信息
     *
     * @param token
     * @return
     */
    @PostMapping("/handleSelectBusinessManagerDiySetting")
    public String handleSelectBusinessManagerDiySetting(@RequestBody String token) {
        logger.info("企业后台-----请求查看总后台企业的自定义设置信息==" + token);
        String s = JSON.parse(token).toString();
        DiySetVo diySetVo = enterpriseManagementService.selectBusinessDiySettingByBid(s);
        logger.info("企业后台查询的自定义设置==" + diySetVo);
        String jsonString = JSON.toJSONString(diySetVo);
        return jsonString;
    }

    /**
     * 暂停过去24小时已经到期的企业和第三方企业
     * 每天凌晨5分暂停过去24小时到期的企业
     */
    @Scheduled(cron = "0 5 0 ? * *")
//    @Scheduled(fixedRate = 50000)
    public void handlePauseBusiness() {
        logger.info("暂停到期24小时以上的企业和第三方企业");
        Date now = new Date();
        //查询过去24h及以上到期并且还处于合作中的企业 即没有被暂停的企业 将其暂停合作 2020-3-9
        List<Map> list = enterpriseManagementService.selectExpireBusinessMoreThan24Hours(returnBefore24HAgo(1, now));
        //没有到期的企业则不执行以下操作
        if (list.size() != 0) {
            //遍历企业修改合作状态 通过bid修改
            List bids = new ArrayList();
            for (Map map : list) {
                String bid = map.get("bid").toString();
                bids.add(bid);
            }
            logger.info("需要更新企业合作状态的企业bid集合===" + bids);
            //总后台修改企业的合作状态
            boolean b = enterpriseManagementService.updateBusinessStatusByBids(bids);
            //  企业后台修改企业合作状态
            String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/user/handleAutoPauseOperation", JSONArray.toJSONString(bids));
            if ("false".equals(s) || !b) {
                logger.error("批量修改企业后台合作状态失败 或更新数据失败");
            }
        }

        //暂停第三方企业
        //查询过去24h到期并且还处于合作中的第三方企业 即没有被暂停的企业 将其暂停合作
        List<Map> listT = dlbsAuthorizationManagementImp.selectAllExpireTBusiness(returnBefore24HAgo(1, now), now);
        //没有到期的企业则不执行以下操作
        if (listT.size() != 0) {
            //遍历企业修改合作状态 通过bid修改  todo 待测试
            List tbids = new ArrayList();
            for (Map map : listT) {
                String bid = map.get("bid").toString();
                tbids.add(bid);
            }
            //修改第三方企业的合作状态
            boolean b = dlbsAuthorizationManagementImp.updateTBusinessStatus(tbids);
            //修改企业后台失败 或更新数据失败
            if (!b) {
                logger.error(" 暂停第三方到期企业失败");
            }
        }
    }

    /**
     * 返回过去24小时的时间点
     *
     * @param number 天数 1
     * @param now    结束时间
     * @return 返回时间差 number=1 返回1天前的时间点
     */
    public Date returnBefore24HAgo(Integer number, Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        //正数代表未来某个时间段 负数代表当前时间前某个时间段
        calendar.add(Calendar.DAY_OF_MONTH, -number);
        Date startT = calendar.getTime();
        logger.info("returnBefore24HAgo=" + sdf.format(startT));
        return startT;
    }


    /**
     * 企业空间管理---页面刷新查询企业存储空间
     *
     * @return
     */
    @PostMapping("/handleQueryEnterpriseStorageSpace")
    public RespBean handleQueryEnterpriseStorageSpace(String bid) {
        logger.info("企业空间管理---页面刷新查询企业存储空间");
        //查询企业的总存储空间
        Map map = new HashMap();
        map.put("token", bid);
        //  查询企业后台视频，图片，录音，日志，对讲记录，电话记录，广播录音记录所占空间
        String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/system/handleQueryStropTip", JSONObject.toJSONString(map));
        if ("".equals(s) || s == null) {
            return new RespBean(505, "查询失败，联系管理员");
        }
        //解析数据
        MyResponseEntity myResponseEntity = JSONObject.parseObject(s, MyResponseEntity.class);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(myResponseEntity.getData()));
        String used = String.valueOf(jsonObject.get("used"));
        String images = String.valueOf(jsonObject.get("images"));
        String video = String.valueOf(jsonObject.get("video"));
        String log = String.valueOf(jsonObject.get("log"));
        String talkBack = String.valueOf(jsonObject.get("talkBack"));
        String phoneRecord = String.valueOf(jsonObject.get("phoneRecord"));
        String broadcast = String.valueOf(jsonObject.get("broadcast"));
        DecimalFormat df = new DecimalFormat("0.00");
        String use = df.format(Double.parseDouble(used) / 1024 / 1024);
        String img = df.format(Double.parseDouble(images) / 1024 / 1024);
        String vid = df.format(Double.parseDouble(video) / 1024 / 1024);
        String lo = df.format(Double.parseDouble(log) / 1024 / 1024);
        String talk = df.format(Double.parseDouble(talkBack) / 1024 / 1024);
        String phone = df.format(Double.parseDouble(phoneRecord) / 1024 / 1024);
        String broad = df.format(Double.parseDouble(broadcast) / 1024 / 1024);
        Map map1 = new HashMap();
        map1.put("used", use);
        map1.put("images", img);
        map1.put("video", vid);
        map1.put("log", lo);
        map1.put("talkBack", talk);
        map1.put("phoneRecord", phone);
        map1.put("broadcast", broad);
        return new RespBean(200, map1);
    }


    /**
     * 企业空间管理----自定义清除企业存储数据
     *
     * @param bid    企业bid
     * @param option 选项 1照片 2视频 3日志 4对讲记录 5电话录音  6广播记录语音
     * @param days   清理的天数
     * @return
     */
    @PostMapping("/handleDeleteEnterpriseStorageSpaceByCondition")
    public RespBean handleDeleteEnterpriseStorageSpaceByCondition(String bid, String option, Integer days) {
        logger.info("企业空间管理----自定义清除企业存储数据==" + option + "==" + bid + "===" + days);
        Map map = new HashMap();
        map.put("token", bid);
        map.put("options", JSONArray.parseArray(option).toJavaList(Integer.class));
        map.put("days", days);
        //删除
        String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/system/handleDeleteStropTip", JSONObject.toJSONString(map));
        if ("false".equals(s) || "".equals(s)) {
            return new RespBean(ResponseCode.FAILURE, "删除失败联系管理员");
        }
        //日志
        BusinessManager businessManager = (BusinessManager) enterpriseManagementService.selectEnterpriseDetail(bid).get("businessManager");
        iLogInfService.addLogInfRecord("删除企业空间数据", 1, businessManager.getMainAccount());
        return new RespBean(ResponseCode.SUCCESS_CODE, "成功");
    }

}
