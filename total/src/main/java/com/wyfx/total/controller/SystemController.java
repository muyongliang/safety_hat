package com.wyfx.total.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wyfx.total.entity.DefaultSetting;
import com.wyfx.total.entity.Dictionaries;
import com.wyfx.total.entity.PageSetting;
import com.wyfx.total.entity.TopMessages;
import com.wyfx.total.entity.vo.MyResponseEntity;
import com.wyfx.total.exception.UploadFileContentTypeException;
import com.wyfx.total.service.*;
import com.wyfx.total.service.impl.DlbsAuthorizationManagementImp;
import com.wyfx.total.utile.HttpClientUtil;
import com.wyfx.total.utile.RespBean;
import com.wyfx.total.utile.ResponseCode;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/system")
public class SystemController extends BaseController {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final List<String> FILE_TYPE = new ArrayList<>();
    protected static Logger logger = LoggerFactory.getLogger(SystemController.class);
    /**
     * 默认设置
     */
    @Autowired
    private IDefaultSettingService iDefaultSettingService;
    /**
     * 系统消息
     */
    @Autowired
    private ITopMessagesService iTopMessagesService;
    @Autowired
    private ILogInfService iLogInfService;
    /**
     * 企业
     */
    @Autowired
    private EnterpriseManagementService enterpriseManagementService;
    /**
     * 第三方企业
     */
    @Autowired
    private DlbsAuthorizationManagementImp dlbsAuthorizationManagementImp;
    //上传文件保存路径
    @Value("${file.uploadFolder}")
    private String uploadFolder;
    //对外暴露访问路径
    @Value("${file.staticAccessPath}")
    private String staticAccessPath;
    @Autowired
    private ISystemService iSystemService;
    @Autowired
    private IDictionaryService iDictionaryService;

    /**
     * 添加默认设置 其他设置
     * 暂时未用到 统一通过更新来实现添加
     *
     * @param defaultSetting
     * @return
     */
    @Deprecated
    @PostMapping("/handleAddDefaultSetting")
    public RespBean handleAddDefaultSetting(DefaultSetting defaultSetting) {
        logger.info("添加默认设置 其他设置");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        iDefaultSettingService.addDefaultSetting(defaultSetting);
        iLogInfService.addLogInfRecord("添加企业默认设置", 0, "添加人：" + request.getSession().getAttribute("userName"));
        return new RespBean(ResponseCode.SUCCESS_CODE, "添加默认设置成功");
    }

    /**
     * 更新系统默认设置
     *
     * @param defaultSetting
     * @return
     */
    @RequestMapping("/handleUpdateDefaultSetting")
    public RespBean handleUpdateDefaultSetting(DefaultSetting defaultSetting) {
        logger.info("添加和编辑系统默认设置===" + defaultSetting.toString());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        iDefaultSettingService.updateDefaultSetting(defaultSetting);
        iLogInfService.addLogInfRecord("更新企业默认设置", 0, "更新人：" + request.getSession().getAttribute("userName"));
        return new RespBean(ResponseCode.SUCCESS_CODE, "更新系统默认设置成功");
    }

    /**
     * 查询默认设置
     *
     * @return
     */
    @RequestMapping("/handleQueryDefaultSetting")
    public RespBean handleQueryDefaultSetting() {
        DefaultSetting defaultSetting = iDefaultSettingService.findDefaultSetting();
        return new RespBean(ResponseCode.SUCCESS_CODE, defaultSetting);
    }

    /**
     * 消息中心--------------------- 定时生成消息 并向系统消息中添加消息
     * <p>
     * 每天0点5分执行暂停过去24小时到期的企业(enterpriseManagerController)
     * 每天0点10执行生成消息 并向系统消息中添加消息
     */
//     @Scheduled(fixedRate = 60000)//60s测一次
    @Scheduled(cron = "55 59 23 1/1 * ?")
    public void task() {

        logger.info("消息中心-------------查看企业到期时间并推送消息给企业 每天11点59分55秒执行");
        //查询默认设置中的 到期提醒时间设置 和存储空间设置
        DefaultSetting defaultSetting = iDefaultSettingService.findDefaultSetting();
        //企业到期提醒时间
        Integer tipTime = defaultSetting.getTipTime();
        Date now = new Date();
        //所有xxx天后到期的合作中的企业列表
        List<Map> list = enterpriseManagementService.selectAllExpireBusiness(now, returnAfterDays(tipTime, now));
        logger.info("合作即将到期的企业列表=" + list);
        //如果有合作即将到期的企业才执行添加消息和向企业后台推送提醒
        //1 添加系统消息
        if (list.size() != 0) {
            //遍历列表并生成系统消息
            for (Map map : list) {
                String banme = (String) map.get("bname");
                Date validityTime = (Date) map.get("validity_time");
                TopMessages topMessages = new TopMessages();
                topMessages.setCompany(banme);
                //0未读取 1已读取
                topMessages.setFlag(0);
                topMessages.setEndTime(validityTime);
                topMessages.setCtime(new Date());
                // 0:"企业服务到期提醒";1:"第三方企业服务到期提醒";2:"企业存储空间报警提醒"}
                topMessages.setType(0);
                //添加系统消息
                iTopMessagesService.addTopMessages(topMessages);
            }

            // 2  todo  去掉 将企业到期的消息推送到企业后台
            List pushBList = new ArrayList();
            for (Map map : list) {
                Map pushMap = new HashMap();
                pushMap.put("title", "企业服务时间到期");
                pushMap.put("content", "合作企业【" + map.get("bname") + "】服务时间即将在【" + sdf.format(map.get("validity_time")) + "】 到期，请提前告知。");
                pushMap.put("type", 0);//消息类型{0:官方消息;其余值为报警类型的值}
                pushMap.put("token", map.get("bid"));//企业的token
                pushBList.add(pushMap);
            }
            //执行推送
            HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/messagesCenter/handleAddScheduledPush", JSON.toJSONString(pushBList));
        }

        //第三方企业到期提醒时间  todo 待测试
        Integer thirdTipTime = defaultSetting.getThirdTipTime();
        List<Map> listT = dlbsAuthorizationManagementImp.selectAllExpireTBusiness(now, returnAfterDays(thirdTipTime, now));
        logger.info("合作即将到期的第三方企业列表=" + listT);
        //1 添加系统消息
        if (listT.size() != 0) {
            //遍历列表并生成系统消息
            for (Map map : listT) {
                String banme = (String) map.get("bname");
                Date validityTime = (Date) map.get("validity_time");
                TopMessages topMessages = new TopMessages();
                topMessages.setCompany(banme);
                //0未读取 1已读取
                topMessages.setFlag(0);
                topMessages.setEndTime(validityTime);
                topMessages.setCtime(new Date());
                // 0:"企业服务到期提醒";1:"第三方企业服务到期提醒";2:"企业存储空间报警提醒"}
                topMessages.setType(1);
                //添加系统消息
                iTopMessagesService.addTopMessages(topMessages);
            }
        }


        //  查询企业存储空间预警的企业列表  todo  2020-2-13 还未测试
        //1 查询所有企业的bid
        List<Map> bidList = enterpriseManagementService.selectAllBusinessASBid();
        //2 筛选出需要预警通知的企业id列表
        List<Map> list2 = new ArrayList<>();
        for (int i = 0; i < bidList.size(); i++) {
            //查询所有的企业存储空间预警值单位GB
            Integer storeTip1 = enterpriseManagementService.selectBusinessDiySettingByBid((String) bidList.get(i).get("bid")).getStoreTip();
            //查询企业后台所有企业储存空间的实际使用值
            Map mapToken = new HashMap((int) (2 / 0.75) + 1);
            mapToken.put("token", bidList.get(i).get("bid"));
            logger.info("mapToken===" + mapToken);
            String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/system/handleQueryStropTip", JSONObject.toJSONString(mapToken));
            //解析数据
            MyResponseEntity myResponseEntity = JSONObject.parseObject(s, MyResponseEntity.class);
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(myResponseEntity.getData()));
            String used = String.valueOf(jsonObject.get("used"));
            DecimalFormat df = new DecimalFormat("0.00");
            String use = df.format(Double.parseDouble(used) / 1024 / 1024 / 1024);//解析为GB
            //添加实际使用值大于预警的企业到通知列表
            if (Double.parseDouble(use) >= storeTip1) {
                Map map = new HashMap((int) (3 / 0.75F) + 1);
                map.put("storeTip", Double.parseDouble(use));
                map.put("bid", bidList.get(i).get("bid"));
                map.put("bname", bidList.get(i).get("bname"));
                list2.add(map);
            }
        }

        logger.info("储存空间预警的企业列表" + list2);
        // 3 如果有企业的存储空间预警时才生成提醒消息
        if (list2.size() != 0) {
            //遍历列表并生成系统消息
            for (Map map : list2) {
                String banme = (String) map.get("bname");
                TopMessages topMessages = new TopMessages();
                topMessages.setCompany(banme);
                //0未读取 1已读取
                topMessages.setFlag(0);
                topMessages.setStore(String.valueOf(map.get("storeTip")));
                topMessages.setCtime(new Date());
                //0:"企业服务到期提醒";1:"第三方企业服务到期提醒";2:"企业存储空间报警提醒"}
                topMessages.setType(2);
                //添加系统消息
                iTopMessagesService.addTopMessages(topMessages);
            }

            // 4  todo  去掉推送 将企业存储空间报警推送给企业后台
//            List pushStopBList = new ArrayList();
//            for (Map s : list2) {
//                Map pushMap = new HashMap();
//                pushMap.put("title", "企业存储空间预警");
//                pushMap.put("type", 0);
//                //查询企业名字
//                BusinessManager o = (BusinessManager) enterpriseManagementService.selectEnterpriseDetail((String) s.get("bid")).get("businessManager");
//                pushMap.put("content", "企业【" + o.getBname() + "】的存储空间已达到该企业【" + s.get("storeTip") + "】的限制，请进行处理");
//                pushMap.put("token", o.getBid());
//                pushStopBList.add(pushMap);
//            }
            //  执行推送到企业后台
            //HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/messagesCenter/handleAddScheduledPush", JSON.toJSONString(pushStopBList));
        }

    }

    /**
     * 返回xxx天后的时间
     *
     * @param days 未来多少天
     * @param now  当前时间
     * @return
     */
    public Date returnAfterDays(Integer days, Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        Date endT = calendar.getTime();
        logger.info("时间从" + sdf.format(now) + "到" + sdf.format(endT) + "之间");
        return endT;
    }

    /**
     * 消息中心----查询系统消息显示在后台
     */
    @GetMapping("/handleQueryTopMessages")
    public RespBean handleQueryTopMessages(Integer flag, Integer pageNum, Integer pageSize) {
        logger.info("消息中心----定时查询系统消息显示在后台并推送给企业管理后台");
        //0未读 1 已读
        Integer f;
        if (flag == null || "".equals(flag)) {
            f = 0;
        } else {
            f = flag;
        }
        logger.info("handleQueryTopMessages（）查询所有消息=flag固定值为null");
        Map mapList = iTopMessagesService.QueryAllTopMessagesByFlag(null, pageNum, pageSize);
        return new RespBean(ResponseCode.SUCCESS_CODE, mapList);
    }

    /**
     * 消息中心----更新已读消息状态flag    暂时通过点击某一条消息来实现更新消息状态
     *
     * @param mids
     * @return
     */
    @PostMapping("/handleUpdateReadTopMessages")
    public RespBean handleUpdateReadTopMessages(String mids) {
        logger.info("消息中心----更新已读消息状态flag mids================" + mids);
        List<Long> list = JSONArray.parseArray(mids).toJavaList(Long.class);
        boolean b = iTopMessagesService.updateWhoIsRead(list);
        return new RespBean(ResponseCode.SUCCESS_CODE, "ok");
    }

    /**
     * 消息中心--- 查询是否存在未读消息
     *
     * @return
     */
    @GetMapping("/handleAllUnReadTopMessages")
    public RespBean handleAllUnReadTopMessages() {
        logger.info("消息中心--- 查询是否存在未读消息");
        List<Long> longs = iTopMessagesService.handleSelectAllUnreadMessages();
        return new RespBean(ResponseCode.SUCCESS_CODE, longs);
    }

    //定义上传文件允许类型
    @PostConstruct
    public void init() {
        FILE_TYPE.add("image/jpeg");
        FILE_TYPE.add("image/png");
    }

    /**
     * 添加系统设置---页面设置
     *
     * @param logo
     * @param meta
     * @param systemName
     * @return
     */
    @RequestMapping("/handleAddPageSetting")
    public RespBean handleAddPageSetting(MultipartFile logo, MultipartFile meta, String systemName) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String logoFilePath = null;
        String metaFilePath = null;
        //上传文件为空则不执行上传操作
        if (logo != null && meta != null) {
            logger.info("添加系统设置---页面设置");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            // 用户上传的文件存储到的文件夹
            File parentDir = new File(uploadFolder);
            // 确保文件夹存在
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            if (!FILE_TYPE.contains(logo.getContentType()) || !FILE_TYPE.contains(meta.getContentType())) {
                throw new UploadFileContentTypeException("上传文件类型错误,允许上传文件类型为：" + FILE_TYPE);
            }
            String logoFileName = sdf.format(new Date()) + "_logo" + "." + FilenameUtils.getExtension(logo.getOriginalFilename());
            logoFilePath = staticAccessPath.substring(0, staticAccessPath.lastIndexOf("/") + 1) + logoFileName;
            // 用户上传的文件存储到服务器端的文件对象
            File destLogo = new File(parentDir, logoFileName);
            String metaFileName = sdf.format(new Date()) + "_meta" + "." + FilenameUtils.getExtension(meta.getOriginalFilename());
            metaFilePath = staticAccessPath.substring(0, staticAccessPath.lastIndexOf("/") + 1) + metaFileName;
            // 用户上传的文件存储到服务器端的文件对象
            File destMeta = new File(parentDir, metaFileName);
            try {
                logo.transferTo(destLogo);
                meta.transferTo(destMeta);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PageSetting pageSetting = new PageSetting();
        pageSetting.setLogoUrl(logoFilePath);
        pageSetting.setMetaUrl(metaFilePath);
        pageSetting.setSystemName(systemName);
        //存数据库
        iSystemService.updatePageSetting(pageSetting);
        Map map = new HashMap();
        map.put("logoUrl", logoFilePath);
        map.put("metaUrl", metaFilePath);
        map.put("ManagerName", systemName);
        iLogInfService.addLogInfRecord("添加页面设置", 0, "添加人：" + request.getSession().getAttribute("userName"));
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }

    /**
     * 系统设置----查询页面设置
     *
     * @return
     */
    @GetMapping("/handleSelectPageSetting")
    public RespBean handleSelectPageSetting() {
        Map map = iSystemService.selectPageSetting();
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }

    /**
     * 系统设置---添加字典
     *
     * @return
     */
    @PostMapping("/handleAddDictionary")
    public RespBean handleAddDictionary(Dictionaries dictionaries) {
        logger.info("系统设置---添加字典");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //字典类型 0:终端工种
        dictionaries.setType(0);
        //生成唯一uuid用于更新企业后台device_type表使用
        String uuid = getUUID();
        dictionaries.setUuid(uuid);
        //添加更新时间
        dictionaries.setUpdateTime(new Date());
        boolean b = iDictionaryService.addDictionary(dictionaries);
        //向企业后台插入字典信息数据
        Map jsonMap = new HashMap();
        jsonMap.put("uuid", uuid);
        jsonMap.put("type", dictionaries.getOptionName());//字典类型 T1
        jsonMap.put("status", dictionaries.getFlag());//字典状态 0启用 1禁用
        jsonMap.put("dicType", dictionaries.getType());//标识字典所属类型 暂时只有终端字典用0标识
        String json = JSON.toJSONString(jsonMap);
        String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/device/handleAddDeviceType", json);
        logger.info("向企业后台添加字典数据s===========" + s);
        if (!b && "false".equals(s)) {
            return new RespBean(501, "添加失败，联系管理员");
        }
        iLogInfService.addLogInfRecord("添加字典配置", 0, "添加人：" + request.getSession().getAttribute("userName"));
        return new RespBean(ResponseCode.SUCCESS_CODE, "添加成功");
    }


    /**
     * 查询某条字典数据
     *
     * @return
     */
    @GetMapping("/handleSelectDicByDid")
    public RespBean handleSelectDicByDid(Integer dicId) {
        logger.info("查询某条字典数据");
        Dictionaries dicByDicId = iDictionaryService.findDicByDicId(dicId);
        return new RespBean(ResponseCode.SUCCESS_CODE, dicByDicId);
    }

    /**
     * 编辑字典
     *
     * @param dictionaries
     * @return
     */
    @PostMapping("/handleUpdateDic")
    public RespBean handleUpdateDic(Dictionaries dictionaries) {
        logger.info("编辑字典");
        //获取请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        dictionaries.setUpdateTime(new Date());
        //更新企业后台的数据
        boolean b = iDictionaryService.updateDictionary(dictionaries);
        Map map = new HashMap((int) (4 / 0.75F + 1));
        map.put("type", dictionaries.getOptionName());
        map.put("status", dictionaries.getFlag());
        map.put("dicType", 0);//暂时一直为0表示是终端
        map.put("uuid", iDictionaryService.findDicByDicId(dictionaries.getDid()).getUuid());
        String jsonString = JSON.toJSONString(map);
        String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/device/handleUpdateDeviceType", jsonString);
        if (!b && "false".equals(s)) {
            return new RespBean(501, "更新字典失败，联系管理员");
        }
        iLogInfService.addLogInfRecord("更新字典配置", 2, "更新人：" + request.getSession().getAttribute("userName"));
        return new RespBean(ResponseCode.SUCCESS_CODE, "更新字典成功");
    }


    /**
     * 禁用 字典
     * 禁用企业后台字典
     *
     * @param dicId
     * @return
     */
    @PostMapping("/handleForbidDic")
    public RespBean handleForbidDic(String dicId) {
        logger.info("禁用字典");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //查询被禁用的字典的uuid
        Dictionaries dictionaries = iDictionaryService.findDicByDicId(Integer.parseInt(dicId));
        Map map = new HashMap(3);
        map.put("flag", 1);
        map.put("dicUUID", dictionaries.getUuid());
        //更新企业后台字典禁用和启用状态
        String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/device/handleForbidAndRestartDeviceType", JSON.toJSONString(map));
        if ("true".equals(s)) {
            //禁用字典flag {0:启用;1:禁用}
            boolean b = iDictionaryService.forbidDictionary(Integer.parseInt(dicId), 1);
            if (b) {
                iLogInfService.addLogInfRecord("禁用字典", 2, "更新人：" + request.getSession().getAttribute("userName"));
                return new RespBean(ResponseCode.SUCCESS_CODE, "更新字典状态成功");
            }
            return new RespBean(501, "更新字典状态失败，联系管理员");
        }
        return new RespBean(501, "更新字典状态失败，联系管理员");
    }

    /**
     * 启用 字典
     *
     * @param dicId
     * @return
     */
    @PostMapping("/handleRestartDic")
    public RespBean handleRestartDic(String dicId) {
        logger.info("启用字典");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //查询被启用的字典的uuid
        Dictionaries dictionaries = iDictionaryService.findDicByDicId(Integer.parseInt(dicId));
        Map map = new HashMap(3);
        map.put("flag", 0);
        map.put("dicUUID", dictionaries.getUuid());

        //更新企业后台字典禁用和启用状态
        String s = HttpClientUtil.doPostJson("http://" + businessRemoteUrl + "/device/handleForbidAndRestartDeviceType", JSON.toJSONString(map));
        if ("true".equals(s)) {
            //禁用字典 flag {0:启用;1:禁用}
            boolean b = iDictionaryService.forbidDictionary(Integer.parseInt(dicId), 0);
            if (b) {
                iLogInfService.addLogInfRecord("启用字典", 2, "更新人：" + request.getSession().getAttribute("userName"));
                return new RespBean(ResponseCode.SUCCESS_CODE, "更新字典状态成功");
            }
            return new RespBean(501, "更新字典状态失败，联系管理员");
        }
        return new RespBean(501, "更新字典状态失败，联系管理员");
    }


    /**
     * 查询所有字典设置
     *
     * @return
     */
    @GetMapping("/handleSelectAllDictionary")
    public RespBean handleSelectAllDictionary(Integer pageNum, Integer pageSize) {
        logger.info("查询所有字典设置");
        Map map = iDictionaryService.selectAll(pageNum, pageSize);
        return new RespBean(ResponseCode.SUCCESS_CODE, map);
    }


    /**
     * 上下移动排序
     * <p>
     * 2020-2-26
     *
     * @param step     移动的方向和位移量  step正数向下移  step负数向上移 如id为5 正数一个位移量为6 负数为4
     * @param orderNum 排序字段
     * @return
     */
    @PostMapping("/handleMoveOrderNum")
    public RespBean handleMoveOrderNum(Integer step, Integer orderNum) {
        logger.info("上下移动字典排序");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        boolean b = iDictionaryService.moveOrderNum(null, step, orderNum);
        if (!b) {
            return new RespBean(509, "改变排序失败");
        }
        iLogInfService.addLogInfRecord("移动排序", 2, "更新人：" + request.getSession().getAttribute("userName"));
        return new RespBean(200, "改变排序成功");
    }


    /**
     * 企业后台导入设备时查询是否存在设备型号
     *
     * @param jsonString
     * @return 包含返回 uuid 否则null
     */
    @PostMapping("/handleSelectDeviceType")
    public String handleSelectDeviceType(@RequestBody String jsonString) {
        logger.info("企业后台导入设备时查询是否存在设备型号" + jsonString);
        String s = JSON.parse(jsonString).toString();
        String uuid = iDictionaryService.selectAllDicIfHaveDeviceType(s);
        logger.info("企业后台获取到设备的uuid" + uuid);
        return uuid;
    }


}
