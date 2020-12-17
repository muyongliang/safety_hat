package com.wyfx.total.service;


import com.wyfx.total.entity.DiySetting;
import com.wyfx.total.entity.Thirdbusinessmanager;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DlbsAuthorizationManagementService {
    boolean addManagement(Thirdbusinessmanager thirdbusinessmanager);

    Map findTBusinessManagerByBid(String bid);

    boolean UpdateTBusinessManger(Thirdbusinessmanager thirdbusinessmanager);

    Map getTBusinessManagerList(Integer state, String bname, String address, String startTime, String endTime, Integer pageNum, Integer pageSize) throws ParseException;

    /**
     * 查询某时间段到期的第三方企业
     *
     * @param startT 查询开始时间
     * @param endT   查询截至时间
     * @return
     */
    List<Map> selectAllExpireTBusiness(Date startT, Date endT);

    boolean pauseTBusinessManager(String bid);

    boolean restartTBusinessManager(Thirdbusinessmanager thirdbusinessmanager);

    Map findAllTBusinessManager(int pageNum, int pageSize);

    boolean updateTBusinessStatus(List<String> bids);

    /**
     * 查询自定义设置
     *
     * @param token
     * @return
     */
    DiySetting findDiySetting(String token);

    /**
     * 通过ip 和mac地址验证企业是否存在
     *
     * @param ip
     * @param mac
     * @return
     */
    Map selectTBusinessByIpAndMac(String ip, String mac);


    /**
     * 通过主账号查询第三方企业信息
     *
     * @param mainAccount
     * @return
     */
    Thirdbusinessmanager findTBusinessByMainAccount(String mainAccount);

    /**
     * 更新企业的 license路径  保存在password字段
     *
     * @param bid
     * @return
     */
    boolean updateTBusinessByBid(String bid, String url);


}
