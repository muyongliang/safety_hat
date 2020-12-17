package com.wyfx.total.service;


import com.wyfx.total.entity.BusinessManager;
import com.wyfx.total.entity.DiySetting;
import com.wyfx.total.entity.vo.DiySetVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface EnterpriseManagementService {


    boolean addEnterprise(BusinessManager businessManager, DiySetting diySetting);

    boolean updateEnterprise(BusinessManager businessManager, DiySetting diySetting);

    boolean restPassWord(String bid);

    boolean pauseCooperationState(String bid);

    boolean restartCooperationState(String bid, Date endT);

    Map selectEnterpriseByCondition(BusinessManager businessManager, Date startT, Date endT, Integer pageNum, Integer pageSize);

    Map findAllBusinessManager(int pageNum, int PageSize);

    Map selectEnterpriseDetail(String uuid);

    /**
     * 查询某时间段到期的企业
     *
     * @param startT 查询开始时间
     * @param endT   查询截至时间
     * @return
     */
    List<Map> selectAllExpireBusiness(Date startT, Date endT);

    /**
     * 查询到期超过24小时的企业
     * time  时间指标 表示此时间以前的所有到期企业
     *
     * @return
     */
    List<Map> selectExpireBusinessMoreThan24Hours(Date time);

    /**
     * 查询企业统计
     *
     * @param address    地址
     * @param businessId 企业bid
     * @return
     */
    Map selectEnterpriseStatisticalByAddressAndBid(String address, String businessId);

    List<Object> selectEnterpriseStatisticalByBid(String bid);

    Map selectAllBusinessManagerByState(Integer status, Integer pageNum, Integer pageSize, String bname);

    /**
     * 通过企业名模糊查询
     *
     * @param bname
     * @return
     */
    Map selectBusinessByName(String bname);

    /**
     * 查询所有企业的坐标
     * 通过合作状态查询 2020-2-26
     * 合作状态{0:合作中;1:暂停合作 }
     *
     * @return
     */
    List selectAllBusinessCoordinate(Integer status);

    /**
     * 通过uuid查询企业自定义设置 ----企业后台使用
     *
     * @param bid
     * @return
     */
    DiySetVo selectBusinessDiySettingByBid(String bid);


    /**
     * 批量修改企业的合作状态
     *
     * @param bids
     * @return
     */
    boolean updateBusinessStatusByBids(List<String> bids);

    /**
     * 查询所有企业的bid集合
     *
     * @return
     */
    List<Map> selectAllBusinessASBid();

    /**
     * 通过主账号查询企业
     *
     * @param mainAccount
     * @return
     */
    BusinessManager selectBusinessByMainAccount(String mainAccount);


}
