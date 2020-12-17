package com.wyfx.total.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.total.entity.*;
import com.wyfx.total.entity.vo.DiySetVo;
import com.wyfx.total.exception.*;
import com.wyfx.total.mapper.*;
import com.wyfx.total.service.EnterpriseManagementService;
import com.wyfx.total.service.ILogInfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("enterpriseManagementService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EnterpriseManagementServiceImp implements EnterpriseManagementService {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger logger = LoggerFactory.getLogger(EnterpriseManagementServiceImp.class);
    /**
     * 企业信息表
     */
    @Autowired
    private BusinessManagerMapper businessManagerMapper;
    /**
     * 自定义配置
     */
    @Autowired
    private DiySettingMapper diySettingMapper;
    @Autowired
    private DefaultSettingMapper defaultSettingMapper;
    /**
     * todo 业务层注入另外一个业务层接口
     */
    @Autowired
    private ILogInfService iLogInfService;
    @Autowired
    private DispatchersMapper dispatchersMapper;
    @Autowired
    private ProjectsMapper projectsMapper;
    @Autowired
    private ClientInfosMapper clientInfosMapper;

    /**
     * 查询某时间段到期的企业列表
     *
     * @param startT 查询开始时间
     * @param endT   查询截至时间
     * @return
     */
    @Override
    public List<Map> selectAllExpireBusiness(Date startT, Date endT) {
        //查询过去某个时间段还么有被暂停的企业 statu 0合作中 1暂停
        List<Map> mapList = businessManagerMapper.selectBusinessManagerByCondition(0, null, null, null, startT, endT);
        logger.info("selectAllExpireBusiness=查询到期的合作企业" + mapList);
        return mapList;
    }

    /**
     * 查询到期24小时以上的企业
     * time  时间指标  查询此时间前的到期企业
     * 2020-3-9
     *
     * @return
     */
    @Override
    public List<Map> selectExpireBusinessMoreThan24Hours(Date time) {
        logger.info("查询到期24小时以上的企业");
        List<Map> mapList = businessManagerMapper.selectNoValidityBusinessManager(0, time);
        return mapList;
    }

    /**
     * 添加企业信息
     *
     * @param businessManager 企业信息
     * @param diySetting      自定义设置
     * @return
     */
    @Override
    public boolean addEnterprise(BusinessManager businessManager, DiySetting diySetting)
            throws BusinessNameConflictException, MainAccountConflictException, MainAccountFormatNotMatchException {

        logger.info("添加企业businessManager=" + businessManager);
        logger.info("添加企业diySetting=" + diySetting);

        //如果到期时间小于当前时间这设置为暂停合作 2020-3-20
        if (System.currentTimeMillis() > businessManager.getValidityTime().getTime()) {
            businessManager.setStatus(1);
        }

        //查询企业是否已经存在
        BusinessManager b = businessManagerMapper.selectByBusinessName(businessManager.getBname());
        if (b != null) {
            throw new BusinessNameConflictException("企业名冲突");
        }
        //查询主账号是否存在
//        BusinessManager b1 = businessManagerMapper.selectBusinessByMainAccount(businessManager.getMainAccount());
//        if (b1 != null) {
//            throw new MainAccountConflictException("主账号冲突");
//        }
        //  企业名是否大于20个字符
        if (businessManager.getBname().length() > 20) {
            throw new BnameLengthTooLongException("企业名长度限制在20个字内");
        }


        //判断主账号合法性 3-10位字母数字字符
        String str = businessManager.getMainAccount();
//        boolean boo = str.matches("^(?![\\d]+$)(?![a-zA-Z]+$)(?![^\\da-zA-Z]+$).{3,10}$");
        boolean boo = str.matches("^[0-9A-Za-z]{3,10}$");//3-10数字或字母
        if (!boo) {
            System.out.println("主账号格式错误");
            throw new MainAccountFormatNotMatchException("主账号格式错误");
        }
        //执行添加
        int businessManagerCount = businessManagerMapper.insertSelective(businessManager);
        logger.info("获取插入数据的自增id==========================" + businessManager.getId());
        //设置用户未设置的项为系统默认设置
        int diySettingCount = diySettingMapper.insertSelective(diySetting);
        if (businessManagerCount < 0) {
            logger.error("插入数据到企业表失败");
            return false;
        }
        if (diySettingCount < 0) {
            logger.error("插入数据到自定义表失败");
            return false;
        }
        return true;
    }

    /**
     * 编辑更新企业信息
     */
    @Override
    public boolean updateEnterprise(BusinessManager businessManager, DiySetting diySetting) {
        //设置更新时间
        businessManager.setUtime(new Date());

        //如果到期时间小于当前时间这设置为暂停合作 2020-3-20
        if (System.currentTimeMillis() > businessManager.getValidityTime().getTime()) {
            businessManager.setStatus(1);
        }

        //通过bid更新 而不是id
        int brow = businessManagerMapper.updateByBusinessIdSelective(businessManager);
        int srow = diySettingMapper.updateByPrimaryKeySelective(diySetting);
        if (brow < 1) {
            logger.error("更新数据到企业表失败");
            return false;
        }
        if (srow < 1) {
            logger.info("跟新数据到自定义表失败");
            return false;
        }
        return true;
    }

    /**
     * 重置企业密码
     *
     * @param bid 企业id 即生成的uuid
     * @return
     */
    @Override
    @Transactional
    public boolean restPassWord(String bid) {
        //通过bid查询企业信息
        BusinessManager businessManager = businessManagerMapper.selectByBusinessKey(bid);
        if (businessManager != null) {
            //重置密码与主账号相同
            String mainAccount = businessManager.getMainAccount();
            businessManager.setPassword(mainAccount);
            Integer row = businessManagerMapper.updateByBusinessIdSelective(businessManager);
            if (row < 0) {
                return false;
            }
            iLogInfService.addLogInfRecord("重置企业密码", 2, businessManager.getBname());
            return true;
        }//企业不存在
        return false;
    }

    /**
     * 合作状态操作之暂停合作
     *
     * @param bid
     * @return
     */
    @Override
    @Transactional
    public boolean pauseCooperationState(String bid) {
        BusinessManager businessManager = businessManagerMapper.selectByBusinessKey(bid);
        int status = businessManager.getStatus();
        if (status == 1) {//1暂停合作 0合作中
            logger.error("状态异常，此企业已经暂停合作，请联系管理员。");
            return false;
        } else {
            businessManager.setStatus(1);
        }
        int row = businessManagerMapper.updateByBusinessIdSelective(businessManager);
        if (row < 0) {
            throw new UpdateDataException("跟新数据异常，请联系管理员！");
        }
        iLogInfService.addLogInfRecord("暂停企业合作", 2, businessManager.getBname());
        return true;
    }

    /**
     * 重启企业服务
     *
     * @param bid  企业bid
     * @param endT 服务到期时间
     * @return
     */
    @Override
    @Transactional
    public boolean restartCooperationState(String bid, Date endT) {
        BusinessManager businessManager = businessManagerMapper.selectByBusinessKey(bid);
        int status = businessManager.getStatus();
        if (status == 1) {//1暂停合作 0合作中
            businessManager.setStatus(0);
            businessManager.setValidityTime(endT);
        } else {
            logger.error("状态异常，此企业处于合作中，请联系管理员。");
            return false;
        }
        int row = businessManagerMapper.updateByBusinessIdSelective(businessManager);
        if (row < 0) {
            throw new UpdateDataException("跟新数据异常，请联系管理员！");
        }
        iLogInfService.addLogInfRecord("重启企业", 2, businessManager.getBname());
        return true;
    }

    /**
     * 查询所有合作企业信息
     *
     * @return
     */
    @Override
    public Map findAllBusinessManager(int pageNum, int pageSize) {
        //总条数
        Integer totalCount = businessManagerMapper.selectEnterpriseCount();
        //分页插件 在查询数据前使用，否则不会生效
        PageHelper.startPage(pageNum, pageSize);
        List<Map> business = businessManagerMapper.selectAllBusinessManager();
        Map map = new HashMap();
        //将查询的数据存入分页插件并返回给前端
        PageInfo pageInfo = new PageInfo(business);
        map.put("totalCount", totalCount);
        map.put("businessManager", pageInfo);
        return map;
    }

    /**
     * 企业管理查询  按条件筛选查询
     *
     * @return
     */
    @Override
    public Map selectEnterpriseByCondition(BusinessManager b, Date startT, Date endT, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map> business = businessManagerMapper.selectBusinessManagerByCondition(
                b.getStatus(), b.getAddress(), b.getBname(), b.getMainAccount(), startT, endT);
        logger.info("按条件查询所有的business=" + business);
        PageInfo pageInfo = new PageInfo(business);
        Map map = new HashMap();
        map.put("businessManager", pageInfo);
        return map;
    }

    /**
     * 企业明细  用于查看企业明细  重置密码   添加日志
     *
     * @return
     */
    @Override
    public Map selectEnterpriseDetail(String uuid) throws NotHaveDefaultSettingException {

        Map map = new HashMap();
        BusinessManager businessManager = businessManagerMapper.selectByBusinessKey(uuid);
        businessManager.setPassword("");
        DiySetting diy = diySettingMapper.selectByBid(businessManager.getBid());
        DefaultSetting defaultSetting = defaultSettingMapper.selectByPrimaryKey(1L);

        //获取返回自定义设置信息  调度员上传限制
        if (diy.getDispatcherUploadLimitSys() == 0) {//{0:跟随系统配置;1:无限制;2:限制}
            if (defaultSetting.getDispatcherUploadLimit() == 0) {//系统默认设置----无限制提交0
//                diy.setDispatcherUploadLimitSys(1);
                diy.setDispatcherUploadLimit(0);//0 代表无限制  todo 可以不返回
            } else {//系统默认设置 --有限制提交限制值
                diy.setDispatcherUploadLimit(defaultSetting.getDispatcherUploadLimit()); //todo 可以不返回
            }
        } else if (diy.getDispatcherUploadLimitSys() == 1) {//自定义无限制
            diy.setDispatcherUploadLimit(0); //todo 可以不返回
        } else {//自定义限制
            diy.setDispatcherUploadLimit(diy.getDispatcherUploadLimit());//todo 必须返回  以下同理
        }

        //视频录制限制
        if (diy.getVedioTimeLimitSys() == 0) {//{0:跟随系统配置;1:无限制;2:限制}
            if (defaultSetting.getVedioTimeLimit() == 0) {//系统默认设置无限制提交0
//                diy.setVedioTimeLimitSys(1);
                diy.setVedioTimeLimit(0);
            } else {//系统默认设置 有限制提交限制值
                diy.setVedioTimeLimit(defaultSetting.getVedioTimeLimit());
            }
        } else if (diy.getVedioTimeLimitSys() == 1) {//自定义无限制
            diy.setVedioTimeLimit(0);
        } else {//自定义限制
            diy.setVedioTimeLimit(diy.getVedioTimeLimit());
        }

        //视频上传限制
        if (diy.getIsAutoUploadSys() == 0) {//{0:跟随系统配置;1:无限制;2:限制}
            if (defaultSetting.getIsAutoUpload() == 0) {//系统默认设置无限制提交0
//                diy.setIsAutoUploadSys(1);
                diy.setIsAutoUpload(0);
            } else {//系统默认设置 有限制提交限制值
                diy.setIsAutoUpload(defaultSetting.getIsAutoUpload());
            }
        } else if (diy.getIsAutoUploadSys() == 1) {//自定义无限制
            diy.setIsAutoUpload(0);
        } else {//自定义限制
            diy.setIsAutoUpload(diy.getIsAutoUpload());
        }

        //终端日志清理设置
        if (diy.getAppLogSys() == 0) {//{0:跟随系统配置;1:不自动清理日志2开 自动清理日志的天数}
            if (defaultSetting.getAppLog() == 0) {//系统默认设置 0不自动清理日志
//                diy.setAppLogSys(1);
                diy.setAppLog(0);
            } else {//系统默认设置  自动清理日志的天数
                diy.setAppLog(defaultSetting.getAppLog());
            }
        } else if (diy.getIsAutoUploadSys() == 1) {//1 自定义不自动清理日志
            diy.setIsAutoUpload(0);
        } else {//自定义 自动清理日志的天数
            diy.setIsAutoUpload(diy.getIsAutoUpload());
        }

        //调度员日志清理设置
        if (diy.getDispatcherLogSys() == 0) {//{0:跟随系统配置;1:不自动清理日志2开 自动清理日志的天数}
            if (defaultSetting.getDispatcherLog() == 0) {//系统默认设置 0不自动清理日志
//                diy.setDispatcherLogSys(1);
                diy.setDispatcherLog(0);
            } else {//系统默认设置  自动清理日志的天数
                diy.setDispatcherLog(defaultSetting.getDispatcherLog());
            }
        } else if (diy.getDispatcherLogSys() == 1) {//1 自定义不自动清理日志
            diy.setDispatcherLog(0);
        } else {//自定义 自动清理日志的天数
            diy.setDispatcherLog(diy.getDispatcherLog());
        }

        //企业存储空间限制
        if (diy.getStoreTipSys() == 0) {//企业存储空间报警配置{0:跟随系统配置;1:自定义;}
            diy.setStoreTip(defaultSetting.getStoreTip().intValue());
        } else {//
            diy.setStoreTip(diy.getStoreTip());
        }
        map.put("businessManager", businessManager);
        map.put("diySettingMapper", diy);
        return map;
    }

    /**
     * 企业统计查询
     * 修改到企业后台查询
     * 企业明细---通过id和地址查询
     *
     * @return
     */
    @Override
    @Deprecated
    public Map selectEnterpriseStatisticalByAddressAndBid(String address, String businessId) {
        //查询调度员总数
        List<Dispatchers> dispatchersList = dispatchersMapper.selectByAddressAndBusinessId(address, businessId, null);
        //查询工程总数
        List<Projects> projectsList = projectsMapper.selectByAddressAndBusinessId(address, businessId, null);
        //查询终端总数
        List<ClientInfos> clientInfosList = clientInfosMapper.selectByAddressAndBusinessId(address, businessId, null);

        //总数
        Integer disCount = dispatchersList.size();
        Integer proCount = projectsList.size();
        Integer cliCount = clientInfosList.size();
        //在线数或项目进行中的数量
        //查询调度员在线数
        List<Dispatchers> dispatchersOnLineList = dispatchersMapper.selectByAddressAndBusinessId(address, businessId, 0);
        //查询工程在建数
        List<Projects> projectsOnLineList = projectsMapper.selectByAddressAndBusinessId(address, businessId, 0);
        //查询终端在线数
        List<ClientInfos> clientInfosOnLineList = clientInfosMapper.selectByAddressAndBusinessId(address, businessId, 0);
        Integer disOnLineCount = dispatchersOnLineList.size();
        Integer proOnBuildCount = projectsOnLineList.size();
        Integer cliOnLineCount = clientInfosOnLineList.size();

        //封装数据
        Map map = new HashMap();
        map.put("disCount", disCount);
        map.put("disOnLineCount", disOnLineCount);
        map.put("proCount", proCount);
        map.put("cliCount", cliCount);
        map.put("proOnBuildCount", proOnBuildCount);
        map.put("cliOnLineCount", cliOnLineCount);
        map.put("disOffLineCount", disCount - disOnLineCount);
        map.put("proOffBuildCount", proCount - proOnBuildCount);
        map.put("cliOffLineCount", cliCount - cliOnLineCount);
        return map;

    }

    /**
     * 通过合作状态查询所有企业
     *
     * @param status 状态 0合作中 暂停1  全部null
     * @return
     */
    @Override
    public Map selectAllBusinessManagerByState(Integer status, Integer pageNum, Integer pageSize, String bname) {

        //分页插件 在查询数据前使用，否则不会生效
        PageHelper.startPage(pageNum, pageSize);
        List<Map> businessManagerList = businessManagerMapper.selectAllByStatusAndBname(status, bname);
        Map resMap = new HashMap();
        PageInfo pageInfo = new PageInfo(businessManagerList);
        resMap.put("allBusiness", pageInfo);

        List cli = new ArrayList();
        Map onlinec = new HashMap();
        onlinec.put("name", "合作中");
        onlinec.put("value", businessManagerMapper.selectAllByStatusAndBname(0, null).size());
        cli.add(onlinec);
        Map dislinec = new HashMap();
        dislinec.put("name", "暂停合作");
        dislinec.put("value", businessManagerMapper.selectAllByStatusAndBname(1, null).size());
        cli.add(dislinec);
        resMap.put("status", cli);
        resMap.put("alltotal", businessManagerMapper.selectAllByStatusAndBname(null, null).size());
        if (status == null || "".equals(status)) {
            resMap.put("count", businessManagerMapper.selectAllByStatusAndBname(null, null).size());
        } else if (status == 1) {
            resMap.put("count", businessManagerMapper.selectAllByStatusAndBname(1, null).size());
        } else {
            resMap.put("count", businessManagerMapper.selectAllByStatusAndBname(0, null).size());
        }
        return resMap;
    }

    /**
     * 通过企业名模糊查询
     *
     * @param bname
     * @return
     */
    @Override
    public Map selectBusinessByName(String bname) {

        List<BusinessManager> businessManagers = businessManagerMapper.selectLikeBname(bname);
        List<Object> respList = new ArrayList<>();
        for (BusinessManager businessManager : businessManagers) {
            Map map = new HashMap();
            map.put("bname", businessManager.getBname());
            map.put("bid", businessManager.getBid());
            map.put("status", businessManager.getStatus());
            map.put("address", businessManager.getAddress());
            map.put("longitude", businessManager.getLongitude());
            map.put("latitude", businessManager.getLatitude());
            respList.add(map);
        }
        Map resMap = new HashMap();
        resMap.put("business", respList);
        return resMap;
    }


    /**
     * 首页--查询
     * 修改到企业后台查询
     *
     * @param bid
     * @return
     */
    @Override
    @Deprecated
    public List<Object> selectEnterpriseStatisticalByBid(String bid) {
        //查询调度员总数
        List<Dispatchers> dispatchersList = dispatchersMapper.selectByAddressAndBusinessId(null, bid, null);
        //查询工程总数
        List<Projects> projectsList = projectsMapper.selectByAddressAndBusinessId(null, bid, null);
        //查询终端总数
        List<ClientInfos> clientInfosList = clientInfosMapper.selectByAddressAndBusinessId(null, bid, null);

        //总数
        Integer disCount = dispatchersList.size();
        Integer proCount = projectsList.size();
        Integer cliCount = clientInfosList.size();
        //查询调度员在线数
        List<Dispatchers> dispatchersOnLineList = dispatchersMapper.selectByAddressAndBusinessId(null, bid, 0);
        //查询工程在建数
        List<Projects> projectsOnLineList = projectsMapper.selectByAddressAndBusinessId(null, bid, 0);
        //查询终端在线数
        List<ClientInfos> clientInfosOnLineList = clientInfosMapper.selectByAddressAndBusinessId(null, bid, 0);
        Integer disOnLineCount = dispatchersOnLineList.size();
        Integer proOnBuildCount = projectsOnLineList.size();
        Integer cliOnLineCount = clientInfosOnLineList.size();


        List<Object> list = new ArrayList<>();
        //封装数据
        Map<String, Object> cliMap = new HashMap();
        Map<String, Object> disMap = new HashMap();
        Map<String, Object> proMap = new HashMap();

        List cli = new ArrayList();
        Map onlinec = new HashMap();
        onlinec.put("name", "在线");
        onlinec.put("value", cliOnLineCount);
        cli.add(onlinec);
        Map dislinec = new HashMap();
        dislinec.put("name", "不在线");
        dislinec.put("value", cliCount - cliOnLineCount);
        cli.add(dislinec);
        cliMap.put("clidata", cli);

        List pro = new ArrayList();
        Map onlinep = new HashMap();
        onlinep.put("name", "进行中");
        onlinep.put("value", proOnBuildCount);
        Map dislinep = new HashMap();
        dislinep.put("name", "已结束");
        dislinep.put("value", proCount - proOnBuildCount);
        pro.add(onlinep);
        pro.add(dislinep);
        proMap.put("prodata", pro);

        List dis = new ArrayList();
        Map onlined = new HashMap();
        onlined.put("name", "在线");
        onlined.put("value", disOnLineCount);
        Map dislined = new HashMap();
        dislined.put("name", "不在线");
        dislined.put("value", disCount - disOnLineCount);
        dis.add(onlined);
        dis.add(dislined);
        disMap.put("disdata", dis);
        list.add(cliMap);
        list.add(disMap);
        list.add(proMap);
        return list;
    }

    /**
     * 查询所有企业的坐标
     * 通过合作状态查询
     * 合作状态{0:合作中;1:暂停合作 }
     *
     * @return
     */
    @Override
    public List selectAllBusinessCoordinate(Integer status) {
        List<Map> mapList = businessManagerMapper.selectAllByStatusAndBname(status, null);
        return mapList;
    }


    /**
     * 企业后台使用 -----通过uuid查询企业自定义设置  编辑企业通知企业后台更改了自定义设置
     *
     * @param bid
     * @return
     */
    @Override
    public DiySetVo selectBusinessDiySettingByBid(String bid) {

        //查询企业的自定义设置
        DiySetting diy = diySettingMapper.selectByBid(bid);
        //查询企业的终端账号限制数量
        BusinessManager businessManager = businessManagerMapper.selectByBusinessKey(bid);
        //查询企业默认设置
        DefaultSetting defaultSetting = defaultSettingMapper.selectByPrimaryKey(1L);

        if (diy.getDispatcherUploadLimitSys() == 0) {//{0:跟随系统配置;1:无限制;2:限制}
            if (defaultSetting.getDispatcherUploadLimit() == 0) {//系统默认设置//无限制提交0
                diy.setDispatcherUploadLimitSys(1);
                diy.setDispatcherUploadLimit(0);//限制大小为0 代表无限制
            } else {//系统默认设置 有限制提交限制值
                diy.setDispatcherUploadLimit(defaultSetting.getDispatcherUploadLimit());
            }
        } else if (diy.getDispatcherUploadLimitSys() == 1) {//自定义无限制
            diy.setDispatcherUploadLimit(0);
        } else {//自定义限制
            diy.setDispatcherUploadLimit(diy.getDispatcherUploadLimit());
        }

        //视频录制限制
        if (diy.getVedioTimeLimitSys() == 0) {//{0:跟随系统配置;1:无限制;2:限制}
            if (defaultSetting.getVedioTimeLimit() == 0) {//系统默认设置无限制提交0
                diy.setVedioTimeLimitSys(1);
                diy.setVedioTimeLimit(0);
            } else {//系统默认设置 有限制提交限制值
                diy.setVedioTimeLimit(defaultSetting.getVedioTimeLimit());
            }
        } else if (diy.getVedioTimeLimitSys() == 1) {//自定义无限制
            diy.setVedioTimeLimit(0);
        } else {//自定义限制
            diy.setVedioTimeLimit(diy.getVedioTimeLimit());
        }

        //视频上传限制
        if (diy.getIsAutoUploadSys() == 0) {//{0:跟随系统配置;1:无限制;2:限制}
            if (defaultSetting.getIsAutoUpload() == 0) {//系统默认设置无限制提交0
                diy.setIsAutoUploadSys(1);
                diy.setIsAutoUpload(0);
            } else {//系统默认设置 有限制提交限制值
                diy.setIsAutoUpload(defaultSetting.getIsAutoUpload());
            }
        } else if (diy.getIsAutoUploadSys() == 1) {//自定义无限制
            diy.setIsAutoUpload(0);
        } else {//自定义限制
            diy.setIsAutoUpload(diy.getIsAutoUpload());
        }

        //终端日志清理设置
        if (diy.getAppLogSys() == 0) {//{0:跟随系统配置;1:不自动清理日志2开 自动清理日志的天数}
            if (defaultSetting.getAppLog() == 0) {//系统默认设置 0不自动清理日志
                diy.setAppLogSys(1);
                diy.setAppLog(0);
            } else {//系统默认设置  自动清理日志的天数
                diy.setAppLog(defaultSetting.getAppLog());
            }
        } else if (diy.getIsAutoUploadSys() == 1) {//1 自定义不自动清理日志
            diy.setIsAutoUpload(0);
        } else {//自定义 自动清理日志的天数
            diy.setIsAutoUpload(diy.getIsAutoUpload());
        }

        //调度员日志清理设置
        if (diy.getDispatcherLogSys() == 0) {//{0:跟随系统配置;1:不自动清理日志2开 自动清理日志的天数}
            if (defaultSetting.getDispatcherLog() == 0) {//系统默认设置 0不自动清理日志
                diy.setDispatcherLogSys(1);
                diy.setDispatcherLog(0);
            } else {//系统默认设置  自动清理日志的天数
                diy.setDispatcherLog(defaultSetting.getDispatcherLog());
            }
        } else if (diy.getDispatcherLogSys() == 1) {//1 自定义不自动清理日志
            diy.setDispatcherLog(0);
        } else {//自定义 自动清理日志的天数
            diy.setDispatcherLog(diy.getDispatcherLog());
        }

        //企业存储空间限制
        if (diy.getStoreTipSys() == 0) {//企业存储空间报警配置{0:跟随系统配置;1:自定义;}
            diy.setStoreTip(defaultSetting.getStoreTip().intValue());
        } else {//
            diy.setStoreTip(diy.getStoreTip());
        }

        DiySetVo diySetVo = new DiySetVo(bid,
                businessManager.getDeviceNumLimit(), businessManager.getMainAccount(),
                businessManager.getValidityTime(),
                diy.getIsCall(), diy.getTalkback(), diy.getVedio(),
                diy.getBroadcast(), diy.getMapRange(),
                diy.getTrail(), diy.getListener(),
                diy.getVedioTimeLimit(), diy.getVedioTimeLimitSys(),
                diy.getIsAutoUpload(), diy.getIsAutoUploadSys(),
                diy.getDispatcherUploadLimit(), diy.getDispatcherUploadLimitSys(),
                diy.getAppLog(), diy.getAppLogSys(),
                diy.getDispatcherLog(), diy.getDispatcherLogSys(),
                diy.getStoreTip(), diy.getStoreTipSys()
        );
        System.err.println("获取DiySetVo=" + diySetVo);
        return diySetVo;
    }

    /**
     * 批量修改企业合作状态
     *
     * @param bids
     * @return
     */
    @Override
    public boolean updateBusinessStatusByBids(List<String> bids) {
        //合作状态{0:合作中;1:暂停合作}
        int i = businessManagerMapper.updateBusinessStatusByBid(bids, 1);
        return i >= 0;
    }


    /**
     * 查询所有企业bid集合
     *
     * @return
     */
    @Override
    public List<Map> selectAllBusinessASBid() {

        //通过id查询所有企业的主账号
        List<Map> maps = businessManagerMapper.selectAllBusinessManager();
        List<Map> list = new ArrayList<>();
        for (int i = 0; i < maps.size(); i++) {
            Map map = new HashMap();
            map.put("bid", maps.get(i).get("bid"));
            map.put("bname", maps.get(i).get("bname"));
            list.add(map);
//            String bid = (String) maps.get(i).get("bid");
//            String bname= (String) maps.get(i).get("bname");
//            list.add(bid);
        }
        return list;
    }

    /**
     * 通过主账号查询企业信息
     *
     * @param mainAccount
     * @return
     */
    @Override
    public BusinessManager selectBusinessByMainAccount(String mainAccount) {
        BusinessManager businessManager = businessManagerMapper.selectBusinessByMainAccount(mainAccount);
        return businessManager;
    }
}
