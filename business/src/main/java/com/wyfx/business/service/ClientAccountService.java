package com.wyfx.business.service;

import com.github.pagehelper.PageInfo;
import com.wyfx.business.entity.ClientAccount;
import com.wyfx.business.entity.vo.ClientAccountFindVo;
import com.wyfx.business.entity.vo.ClientAccountSelectVo;
import com.wyfx.business.entity.vo.MyClientAccountVo;

import java.util.List;
import java.util.Map;

public interface ClientAccountService {
    void addClientAccount(ClientAccount clientAccount);

    void updateClientAccount(ClientAccount account);

    void updateClientAccountByBid(Integer bid, ClientAccount account);

    ClientAccount findByClientId(Integer clientId);

    List<ClientAccount> findByDeviceId(Integer deviceId);

    ClientAccount findByBid(Integer bid);

    boolean isAllowAddClientAccount(String token);

    boolean isAllowChangeStatus(String token);

    /**
     * 根据状态查询项目下的终端账号
     *
     * @param status
     * @param projectId
     * @return
     */
    PageInfo findByStatus(Integer status, Integer projectId, int pageSize, int pageNum);

    /**
     * 根据姓名查找
     *
     * @param projectId
     * @param name
     * @return
     */
    PageInfo<ClientAccountFindVo> findByName(Integer projectId, String name, Integer pageSize, Integer pageNum);

    /**
     * 根据电话查找
     *
     * @param projectId
     * @param tel
     * @return
     */
    PageInfo<ClientAccountFindVo> findByTel(Integer projectId, String tel, Integer pageSize, Integer pageNum);

    /**
     * 根据账号查询
     *
     * @param projectId
     * @param account
     * @return
     */
    PageInfo<ClientAccountFindVo> findByAccount(Integer projectId, String account, Integer pageSize, Integer pageNum);

    /**
     * 根据设备编号查询
     *
     * @param projectId
     * @param number
     * @return
     */
    PageInfo<ClientAccountFindVo> findByNumber(Integer projectId, String number, Integer pageSize, Integer pageNum);

    /**
     * 查询所有的终端账号
     *
     * @param projectId
     * @return
     */
    PageInfo findAllAccount(Integer projectId, int pageSize, int pageNum);


    Map getCurrentClientInfo(Long bid);

    /**
     * 导出终端账号excel表
     *
     * @param bids
     * @return
     */
    List<ClientAccountSelectVo> exportClientAccountExcelList(Integer projectId, List<Long> bids);

    /**
     * 企业总后台查询终端账号信息
     *
     * @param myClientAccountVo
     * @return
     */
    PageInfo selectClientAccountByCondition(MyClientAccountVo myClientAccountVo);

    /**
     * 根据在线状态查询终端设备
     *
     * @param onlineStatus
     * @param pageSize
     * @param pageNum
     * @return
     */
    PageInfo findClientAccountByOnlineStatus(Integer onlineStatus, Long businessId, Integer pageSize, Integer pageNum);

    /**
     * 总后台首页查询某公司的调度员和终端在线情况 以及工程情况
     *
     * @param
     * @param token
     * @return
     */
    List<Object> selectCountByToken(String token);

    /**
     * 总后台企业管理 -企业明细- 统计查询 通过地址查询
     *
     * @param bid
     * @param address
     * @return
     */
    Map selectCountByAddress(String bid, String address);

    /**
     * 查询企业所有的终端账号数量
     *
     * @param token
     * @return
     */
    Integer selectAllClientCountByToken(String token);

    String findIMEIByBid(Long bid);
}
