package com.wyfx.total.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.total.entity.DiySetting;
import com.wyfx.total.entity.Thirdbusinessmanager;
import com.wyfx.total.entity.vo.BusinessInfo;
import com.wyfx.total.exception.*;
import com.wyfx.total.mapper.DefaultSettingMapper;
import com.wyfx.total.mapper.DiySettingMapper;
import com.wyfx.total.mapper.ThirdbusinessmanagerMapper;
import com.wyfx.total.service.DlbsAuthorizationManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("dlbsAuthorizationManagementService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DlbsAuthorizationManagementImp implements DlbsAuthorizationManagementService {


    /**
     * 第三方企业
     */
    @Autowired
    private ThirdbusinessmanagerMapper thirdbusinessmanagerMapper;

    @Autowired
    private DiySettingMapper diySettingMapper;

    @Autowired
    private DefaultSettingMapper defaultSettingMapper;

    /**
     * 添加第三方企业
     *
     * @param thirdbusinessmanager
     * @return
     */
    @Override
    public boolean addManagement(Thirdbusinessmanager thirdbusinessmanager) throws BusinessNameConflictException, InsertDataException {
        //查看添加的第三方企业名是否重复 通过企业名查询第三方企业信息
        Thirdbusinessmanager t = thirdbusinessmanagerMapper.selectByTBusinessName(thirdbusinessmanager.getBname());
        if (t != null) {
            throw new BusinessNameConflictException("第三方企业名重复。");
        }
        // 判断企业名是否大于20个字
        if (thirdbusinessmanager.getBname().length() > 20) {
            throw new BnameLengthTooLongException("第三方企业名长度限制在20个字内");
        }

        //判断提交服务到期时间是否为当前时间之前 如何是则设置合作状态为暂停合作
        if (System.currentTimeMillis() > thirdbusinessmanager.getValidityTime().getTime()) {
            thirdbusinessmanager.setState(1);
        }
        int thirdbusinessmanagerCount = thirdbusinessmanagerMapper.insertSelective(thirdbusinessmanager);
        if (thirdbusinessmanagerCount < 0) {
            throw new InsertDataException("插入数据异常，请联系管理员！");
        }
        return true;
    }

    /**
     * 按条件查询第三方企业
     *
     * @param state
     * @param bname
     * @param address
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Map getTBusinessManagerList(Integer state, String bname, String address, String startTime, String endTime, Integer pageNum, Integer pageSize) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//2020-1-19 update
        Date startT;
        Date endT;
        if ("".equals(startTime) || startTime == null) {
            startT = null;
        } else {
            startT = sdf.parse(startTime);
        }
        if ("".equals(endTime) || endTime == null) {
            endT = null;
        } else {
            endT = sdf.parse(endTime);
        }

        PageHelper.startPage(pageNum, pageSize);
        //查询结果
        List<Map> list = thirdbusinessmanagerMapper.selectTBusinessManagerByCondition(state, bname, address, startT, endT);
        PageInfo pageInfo = new PageInfo(list);
        Map map = new HashMap();
        map.put("TBusiness", pageInfo);
        return map;
    }

    /**
     * 通过第三方企业bid查询企业明细
     *
     * @param bid 企业bid
     * @return
     * @throws BusinessMessagesIsNotExistException
     */
    @Override
    public Map findTBusinessManagerByBid(String bid) throws BusinessMessagesIsNotExistException {
        Thirdbusinessmanager thirdbusinessmanager = thirdbusinessmanagerMapper.selectTBusinessByBid(bid);
        if (thirdbusinessmanager == null) {
            throw new BusinessMessagesIsNotExistException("企业数据不存在");
        }
//        DiySetting diySetting = diySettingMapper.selectByBid(bid);
//        DefaultSetting defaultSetting = defaultSettingMapper.selectByPrimaryKey(1L);
//        if (diySetting.getStoreTipSys() == 0) {//企业存储空间报警配置{0:跟随系统配置;1:自定义;}
//            diySetting.setStoreTip(defaultSetting.getStoreTip().intValue());
//        }
        System.out.println("查询第三方企业详情=" + thirdbusinessmanager);
        Map map = new HashMap();
        map.put("thirdbusinessmanager", thirdbusinessmanager);
//        map.put("diySetting", diySetting);
        return map;
    }

    /**
     * 更新第三方企业信息
     *
     * @param thirdbusinessmanager
     * @return
     */
    @Override
    public boolean UpdateTBusinessManger(Thirdbusinessmanager thirdbusinessmanager) throws BusinessNameConflictException, InsertDataException {
        //查看添加的第三方企业名是否重复 通过企业名查询第三方企业信息
        Thirdbusinessmanager t = thirdbusinessmanagerMapper.selectByTBusinessName(thirdbusinessmanager.getBname());
//        if (t != null) {
//            throw new BusinessNameConflictException("第三方企业名重复。");
//        }
        // 判断企业名是否大于20个字
//        if (thirdbusinessmanager.getBname().length() > 20) {
//            throw new BnameLengthTooLongException("第三方企业名长度限制在20个字内");
//        }
        //判断提交服务到期时间是否为当前时间之前 如何是则设置合作状态为暂停合作
        if (System.currentTimeMillis() > thirdbusinessmanager.getValidityTime().getTime()) {
            thirdbusinessmanager.setState(1);
        }
        //更新
        Integer row = thirdbusinessmanagerMapper.updateTBusinessByBid(thirdbusinessmanager);
        if (row < 0) {
            throw new InsertDataException("更新数据异常，请联系管理员！");
        }
        return true;
    }


    /**
     * 暂停第三方企业合作
     *
     * @param bid
     * @return
     */
    @Override
    public boolean pauseTBusinessManager(String bid) throws UpdateDataException {
        Thirdbusinessmanager t = thirdbusinessmanagerMapper.selectTBusinessByBid(bid);
        //执行状态更改
        t.setState(1);
        Integer row = thirdbusinessmanagerMapper.updateByPrimaryKeySelective(t);
        if (row < 0) {
            throw new UpdateDataException("更新第三方企业信息错误，联系管理员！");
        }
        return true;
    }

    /**
     * 重启第三方企业合作
     *
     * @param thirdbusinessmanager
     * @return
     */
    @Override
    public boolean restartTBusinessManager(Thirdbusinessmanager thirdbusinessmanager) throws DateFormatException, UpdateDataException {
        Thirdbusinessmanager t = thirdbusinessmanagerMapper.selectTBusinessByBid(thirdbusinessmanager.getBid());

        if (System.currentTimeMillis() > thirdbusinessmanager.getValidityTime().getTime()) {
            throw new DateFormatException("启用到期时间必须大于当前时间");
        }
        t.setValidityTime(thirdbusinessmanager.getValidityTime());
        t.setState(0);
        Integer row = thirdbusinessmanagerMapper.updateTBusinessByBid(t);
        if (row < 0) {
            throw new UpdateDataException("开启合作状态失败，联系管理员");
        }
        return true;
    }


    /**
     * 查询所有第三方企业 按跟新时间降序排列
     *
     * @param pageNum  分页数
     * @param pageSize 每一页数据条数
     * @return
     */
    @Override
    public Map findAllTBusinessManager(int pageNum, int pageSize) {
        //查询第三方总数
        Integer count = thirdbusinessmanagerMapper.selectTBusinessManagerCount();
        //分页插件 在查询数据前使用，否则不会生效
        PageHelper.startPage(pageNum, pageSize);
        List<Map> thirdbusinessmanager = thirdbusinessmanagerMapper.selectAllTBusinessManager();
        //将查询的数据存入分页插件并返回给前端
        PageInfo pageInfo = new PageInfo(thirdbusinessmanager);
        Map respMap = new HashMap((int) (2 / 0.75F) + 1);
        respMap.put("thirdBussinessManager", pageInfo);
        respMap.put("AllCount", count);
        return respMap;
    }


    /**
     * 查询某时间段到期的第三方企业
     *
     * @param startT 查询开始时间
     * @param endT   查询截至时间
     * @return
     */
    @Override
    public List<Map> selectAllExpireTBusiness(Date startT, Date endT) {

        List<Map> mapList = thirdbusinessmanagerMapper.selectTBusinessManagerByCondition(null, null, null, startT, endT);
        System.err.println("查询到期的第三方企业" + mapList);
        return mapList;
    }

    /**
     * 批量修改 第三方企业合作状态
     *
     * @param bids
     * @return
     */
    @Override
    public boolean updateTBusinessStatus(List<String> bids) {
        //合作状态{0:合作中;1:暂停合作}
        int i = thirdbusinessmanagerMapper.updateTBusinessStatusByBids(bids, 1);
        return i >= 0;
    }

    /**
     * 查询自定义设置
     *
     * @param token
     * @return
     */
    @Override
    public DiySetting findDiySetting(String token) {
        DiySetting diySetting = diySettingMapper.selectByBid(token);
        return diySetting;
    }

    /**
     * 查询企业是否存在
     *
     * @param ip
     * @param mac
     * @return
     */
    @Override
    public Map selectTBusinessByIpAndMac(String ip, String mac) {
        Thirdbusinessmanager tBusiness = thirdbusinessmanagerMapper.selectByIpAndMac(ip, mac);
        BusinessInfo businessInfo = new BusinessInfo(null, tBusiness.getBname(), tBusiness.getBid(), tBusiness.getMainAccount(), tBusiness.getDeviceNumLimit()
                , tBusiness.getState(), 1);//0普通企业 1第三方企业
        Map map = new HashMap((int) (1 / 0.75F) + 1);
        if (tBusiness == null) {
            map.put("businessInfo", null);
            return map;
        }
        map.put("businessInfo", businessInfo);
        return map;
    }

    /**
     * 通过主账号查询第三方企业信息
     *
     * @param mainAccount
     * @return
     */
    @Override
    public Thirdbusinessmanager findTBusinessByMainAccount(String mainAccount) {
        return thirdbusinessmanagerMapper.selectTBusinessByMainAccount(mainAccount);
    }

    /**
     * 更新license路径
     *
     * @param bid
     * @return
     */
    @Override
    public boolean updateTBusinessByBid(String bid, String url) {
        Thirdbusinessmanager thirdbusinessmanager = new Thirdbusinessmanager();
        thirdbusinessmanager.setBid(bid);
        //password用于保存license路径
        thirdbusinessmanager.setPassword(url);
        int i = thirdbusinessmanagerMapper.updateTBusinessByBid(thirdbusinessmanager);
        return i >= 0;
    }
}

