package com.wyfx.business.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wyfx.business.dao.*;
import com.wyfx.business.entity.ClientAccount;
import com.wyfx.business.entity.DefaultClientVideo;
import com.wyfx.business.entity.vo.ClientAccountFindVo;
import com.wyfx.business.entity.vo.ClientAccountSelectVo;
import com.wyfx.business.entity.vo.MyClientAccountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author johnson liu
 * @date 2019/11/7
 * @description 终端账号业务类
 */
@Service
@Transactional
public class ClientAccountServiceImpl implements ClientAccountService, DefaultClientVideoService {

    @Autowired
    private DefaultClientVideoMapper defaultClientVideoMapper;
    @Autowired
    private ClientAccountMapper clientAccountMapper;

    @Autowired
    private BusinessUserMapper businessUserMapper;
    @Autowired
    private BusinessInfoMapper businessInfoMapper;
    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Override
    @Transactional
    public void addClientAccount(ClientAccount clientAccount) {
        clientAccountMapper.insertSelective(clientAccount);
    }

    @Override
    @Transactional
    public void updateClientAccount(ClientAccount account) {
        clientAccountMapper.updateByPrimaryKeySelective(account);
    }

    @Override
    public void updateClientAccountByBid(Integer bid, ClientAccount account) {
        if (bid == null || bid == 0) {
            return;
        }
        clientAccountMapper.updateByBid(account);
    }

    @Override
    public ClientAccount findByClientId(Integer clientId) {
        return clientAccountMapper.selectByPrimaryKey(clientId.longValue());
    }

    @Override
    public List<ClientAccount> findByDeviceId(Integer deviceId) {
        return clientAccountMapper.selectByDeviceId(deviceId.longValue());
    }

    @Override
    public ClientAccount findByBid(Integer bid) {
        return clientAccountMapper.selectByBid(bid.longValue());
    }

    /**
     * 是否允许添加终端账号
     *
     * @return
     */
    @Override
    public boolean isAllowAddClientAccount(String token) {
        boolean flag = true;
        Integer limit = businessInfoMapper.findBusinessInfoByToken(token).getClientNum();
        Integer count = businessInfoMapper.selectClientNumByBusinessId(token);
        if (count >= limit) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean isAllowChangeStatus(String token) {
        boolean flag = true;
        Integer limit = businessInfoMapper.findBusinessInfoByToken(token).getClientNum();
        Integer count = businessUserMapper.selectUsedClientNumByBusinessId(token);
        if (count >= limit) {
            flag = false;
        }
        return flag;
    }

    /**
     * 根据状态查询项目下的终端账号
     *
     * @param status
     * @param projectId
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo findByStatus(Integer status, Integer projectId, int pageSize, int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<ClientAccountSelectVo> list = clientAccountMapper.findByStatus(status, projectId);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据姓名查找
     *
     * @param projectId
     * @param name
     * @return
     */
    @Override
    public PageInfo<ClientAccountFindVo> findByName(Integer projectId, String name, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum - 1, pageSize);
        List<ClientAccountFindVo> list = clientAccountMapper.findByName(projectId, name);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据电话查找
     *
     * @param projectId
     * @param tel
     * @return
     */
    @Override
    public PageInfo<ClientAccountFindVo> findByTel(Integer projectId, String tel, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum - 1, pageSize);
        List<ClientAccountFindVo> list = clientAccountMapper.findByTel(projectId, tel);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据账号查询
     *
     * @param projectId
     * @param account
     * @return
     */
    @Override
    public PageInfo<ClientAccountFindVo> findByAccount(Integer projectId, String account, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum - 1, pageSize);
        List<ClientAccountFindVo> list = clientAccountMapper.findByAccount(projectId, account);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 根据设备编号查询
     *
     * @param projectId
     * @param number
     * @return
     */
    @Override
    public PageInfo<ClientAccountFindVo> findByNumber(Integer projectId, String number, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum - 1, pageSize);
        List<ClientAccountFindVo> list = clientAccountMapper.findByNumber(projectId, number);
        PageInfo pageInfo = new PageInfo(list);

        return pageInfo;
    }

    /**
     * 查询所有的终端账号
     *
     * @param projectId
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo findAllAccount(Integer projectId, int pageSize, int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<ClientAccountSelectVo> list = clientAccountMapper.findAllAccount(projectId);
        PageInfo pageInfo = new PageInfo(list);

        /*PageHelper.startPage(pageNum, pageSize);
        List<ClientAccountFindVo> list=clientAccountMapper.findAllAccount(projectId);
        PageInfo pageInfo=new PageInfo(list);*/
        return pageInfo;
    }

    /**
     * 更新终端视频默认参数业务
     *
     * @param vid                主键ID
     * @param defaultClientVideo
     */
    @Override
    @Transactional
    public void updateDefaultClientVideoSetting(Long vid, DefaultClientVideo defaultClientVideo) {
        if (vid == null) {
            defaultClientVideoMapper.insertSelective(defaultClientVideo);
        } else {
            defaultClientVideoMapper.updateByPrimaryKeySelective(defaultClientVideo);
        }
    }

    @Override
    public Map getCurrentClientInfo(Long bid) {
        return clientAccountMapper.selectCurrentClientInfo(bid);
    }

    /**
     * 导出终端列表
     *
     * @param projectId
     * @param bids
     * @return
     */
    @Override
    public List<ClientAccountSelectVo> exportClientAccountExcelList(Integer projectId, List<Long> bids) {
        List<ClientAccountSelectVo> clientList = clientAccountMapper.findByBids(projectId, bids);
        System.out.println(clientList);
        return clientList;
    }

    /**
     * 企业总后台查询终端信息
     * create by wsm on 2019-12-3
     *
     * @param myClientAccountVo
     * @return
     */
    @Override
    public PageInfo selectClientAccountByCondition(MyClientAccountVo myClientAccountVo) {
        //可以在mapper.xml中的处理空字符串 <if test="number != null and  number !=''">
        myClientAccountVo.setBname(myClientAccountVo.getBname() == null || "".equals(myClientAccountVo.getBname()) ? null : myClientAccountVo.getBname());
        myClientAccountVo.setOnlineStatus(myClientAccountVo.getOnlineStatus() == null || "".equals(myClientAccountVo.getOnlineStatus()) ? null : myClientAccountVo.getOnlineStatus());
        myClientAccountVo.setName(myClientAccountVo.getName() == null || "".equals(myClientAccountVo.getName()) ? null : myClientAccountVo.getName());
        myClientAccountVo.setUsername(myClientAccountVo.getUsername() == null || "".equals(myClientAccountVo.getUsername()) ? null : myClientAccountVo.getUsername());
        myClientAccountVo.setMobile(myClientAccountVo.getMobile() == null || "".equals(myClientAccountVo.getMobile()) ? null : myClientAccountVo.getMobile());
        myClientAccountVo.setWorkType(myClientAccountVo.getWorkType() == null || "".equals(myClientAccountVo.getWorkType()) ? null : myClientAccountVo.getWorkType());
        myClientAccountVo.setNumber(myClientAccountVo.getNumber() == null || "".equals(myClientAccountVo.getNumber()) ? null : myClientAccountVo.getNumber());
        myClientAccountVo.setClientStatus(myClientAccountVo.getClientStatus() == null || "".equals(myClientAccountVo.getClientStatus()) ? null : myClientAccountVo.getClientStatus());

        PageHelper.startPage(myClientAccountVo.getPageNum(), myClientAccountVo.getPageSize());
        List<MyClientAccountVo> clientByCondition = clientAccountMapper.findClientByCondition(myClientAccountVo);
        PageInfo pageInfo = new PageInfo(clientByCondition);
        return pageInfo;
    }

    /**
     * 根据在线状态查询终端设备
     *
     * @param onlineStatus
     * @return
     */
    @Override
    public PageInfo findClientAccountByOnlineStatus(Integer onlineStatus, Long businessId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map> list = clientAccountMapper.findByOnlineStatus(onlineStatus, businessId);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    /**
     * 总后台首页查询某公司的调度员和终端在线情况 以及工程情况
     *
     * @param
     * @param token
     * @return
     */
    @Override
    public List<Object> selectCountByToken(String token) {
        //终端总数 onlineStatus  0离线 其他在线  userType 1调度员 2 终端
        Integer allC = clientAccountMapper.selectCount(null, token, 2);
        //终端离线数
        Integer disOnlineC = clientAccountMapper.selectCount(0, token, 2);
        Integer onLineC = allC - disOnlineC;
        //调度员总数
        Integer allD = clientAccountMapper.selectCount(null, token, 1);
        //调度员离线数
        Integer disOnlineD = clientAccountMapper.selectCount(0, token, 1);
        Integer onLineD = allD - disOnlineD;

        //项目总数
        Integer allP = projectInfoMapper.selectProjectCount(token, null);
        //在建项目数 0进行中 1 已结束
        Integer onBuildP = projectInfoMapper.selectProjectCount(token, 0);

        Integer disBuildP = allP - onBuildP;

        List<Object> respList = new ArrayList<>();
        //封装数据
        Map<String, Object> cliMap = new HashMap();
        Map<String, Object> disMap = new HashMap();
        Map<String, Object> proMap = new HashMap();
        //终端数据
        List cli = new ArrayList();
        Map onlinec = new HashMap();
        onlinec.put("name", "在线");
        onlinec.put("value", onLineC);
        cli.add(onlinec);
        Map dislinec = new HashMap();
        dislinec.put("name", "不在线");
        dislinec.put("value", disOnlineC);
        cli.add(dislinec);
        cliMap.put("clidata", cli);

        //调度员数据
        List dis = new ArrayList();
        Map onlined = new HashMap();
        onlined.put("name", "在线");
        onlined.put("value", onLineD);
        Map dislined = new HashMap();
        dislined.put("name", "不在线");
        dislined.put("value", disOnlineD);
        dis.add(onlined);
        dis.add(dislined);
        disMap.put("disdata", dis);

        //项目数据
        List pro = new ArrayList();
        Map onlinep = new HashMap();
        onlinep.put("name", "进行中");
        onlinep.put("value", onBuildP);
        Map dislinep = new HashMap();
        dislinep.put("name", "已结束");
        dislinep.put("value", disBuildP);
        pro.add(onlinep);
        pro.add(dislinep);
        proMap.put("prodata", pro);
        respList.add(cliMap);
        respList.add(disMap);
        respList.add(proMap);

        return respList;
    }

    /**
     * 总后台企业管理 -企业明细- 统计查询 通过地址查询
     *
     * @param token
     * @param address
     * @return 返回项目的id集合
     */
    @Override
    public Map selectCountByAddress(String token, String address) {
        //查询所有项目数 项目的id集合
        List<Integer> allP = projectInfoMapper.selectProjectCountByAddressAndStatus(token, null, address);
        System.out.println("项目id集合" + allP);
        if (allP.size() == 0) {
            Map map = new HashMap(2);
            map.put("403", "该地址无项目数据");
            return map;
        }
        //查询已经结束项目数   项目状态{0:进行中;1:已结束}
        List<Integer> disBuildP = projectInfoMapper.selectProjectCountByAddressAndStatus(token, 1, address);
        Integer onBuildP = allP.size() - disBuildP.size();
        //通过项目id查询该项目下的离线终端数 在线状态{0:离线；1:手机在线;2:电脑在线;3:手机电脑同时}
        Integer disOnlineC = clientAccountMapper.selectStatisticsByProjectIdList(allP, 2, 0);
        //所有终端数
        Integer allC = clientAccountMapper.selectStatisticsByProjectIdList(allP, 2, null);
        //在线终端数
        Integer onLineC = allC - disOnlineC;

        //离线调度员
        Integer disOnlineD = clientAccountMapper.selectStatisticsByProjectIdList(allP, 1, 0);
        //所有调度员
        Integer allD = clientAccountMapper.selectStatisticsByProjectIdList(allP, 1, null);
        //在线调度员
        Integer onLineD = allD - disOnlineD;
        Map map = new HashMap(12);

        map.put("disCount", allD);
        map.put("disOnLineCount", onLineD);
        map.put("disOffLineCount", disOnlineD);

        map.put("proCount", allP.size());
        map.put("proOnBuildCount", onBuildP);
        map.put("proOffBuildCount", disBuildP.size());

        map.put("cliCount", allC);
        map.put("cliOnLineCount", onLineC);
        map.put("cliOffLineCount", disOnlineC);

        return map;
    }


    /**
     * 系统设置----服务状态 查询已经分配的所有终端数量
     *
     * @param token
     * @return
     */
    @Override
    public Integer selectAllClientCountByToken(String token) {
        Integer integer = clientAccountMapper.selectCount(null, token, 2);
        return integer;
    }

    @Override
    public String findIMEIByBid(Long bid) {
        return clientAccountMapper.selectIMEIByBid(bid);
    }
}
