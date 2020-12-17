package com.wyfx.business.dao;

import com.wyfx.business.entity.ClientAccount;
import com.wyfx.business.entity.vo.ClientAccountFindVo;
import com.wyfx.business.entity.vo.ClientAccountSelectVo;
import com.wyfx.business.entity.vo.MyClientAccountVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ClientAccountMapper {
    int deleteByPrimaryKey(Long clientId);

    int insert(ClientAccount record);

    int insertSelective(ClientAccount record);

    ClientAccount selectByPrimaryKey(Long clientId);

    ClientAccount selectByBid(Long bid);

    /**
     * 通过终端设备ids查询终端账号信息
     * create by wsm on 2019-11-21
     *
     * @return
     */
    List<ClientAccount> selectByDeviceIds(List<Long> ids);

    List<ClientAccount> selectByDeviceId(@Param("deviceId") Long deviceId);

    /**
     * 通过business_user 的 bid查询终端账号列表
     * create by wsm on 2019-11-22
     *
     * @param bids
     * @return
     */
    List<ClientAccountSelectVo> findByBids(@Param("projectId") Integer projectId, @Param("bids") List<Long> bids);

    /**
     * 企业后台多条件查询终端账号信息
     * create by wsm on 2019-2-12-3
     *
     * @param myClientAccountVo
     * @return
     */
    List<MyClientAccountVo> findClientByCondition(MyClientAccountVo myClientAccountVo);

    /**
     * 首页----通过在线状态查询终端在线与离线终端信息
     *
     * @return
     */
    List<MyClientAccountVo> selectClientByOnLineStatus(@Param("onlineStatus") Integer onlineStatus, @Param("projectId") Integer projectId);


    /**
     * 总后台首页--通过企业id,状态，用户类型 查询终端和调度员在线状态统计
     *
     * @param onlineStatus
     * @param token
     * @return
     */
    Integer selectCount(@Param("onlineStatus") Integer onlineStatus, @Param("token") String token, @Param("userType") Integer userType);

    /**
     * 总后台 --通过地址和企业bid查询某企业终端 及调度员数量统计
     * 具体逻辑通过项目id集合查询项目下的终端 和调度员情况
     *
     * @param projectIdList
     * @param userType      账号类型{1:调度员,0:企业管理员;2:终端账号;3:其他管理员账号}
     * @return
     */
    Integer selectStatisticsByProjectIdList(@Param("projectIdList") List<Integer> projectIdList, @Param("userType") Integer userType, @Param("onlineStatus") Integer onlineStatus);


    int updateByPrimaryKeySelective(ClientAccount record);

    int updateByBid(ClientAccount record);

    int updateByPrimaryKey(ClientAccount record);

    List<ClientAccountSelectVo> findAllAccount(@Param("projectId") Integer projectId);


    List<ClientAccountSelectVo> findByStatus(@Param("status") Integer status, @Param("projectId") Integer projectId);

    //List<ClientAccountFindVo> findAllAccount(@Param("projectId")Integer projectId);

    List<ClientAccountFindVo> findByName(@Param("projectId") Integer projectId, @Param("name") String name);

    List<ClientAccountFindVo> findByTel(@Param("projectId") Integer projectId, @Param("tel") String tel);

    /**
     * 根据账号查询
     *
     * @param projectId
     * @param account
     * @return
     */
    List<ClientAccountFindVo> findByAccount(@Param("projectId") Integer projectId, @Param("account") String account);

    /**
     * 根据设备编号查询
     *
     * @param projectId
     * @param number
     * @return
     */
    List<ClientAccountFindVo> findByNumber(@Param("projectId") Integer projectId, @Param("number") String number);

    /**
     * 根据在线状态查询
     *
     * @param onlineStatus
     * @return
     */
    List<Map> findByOnlineStatus(@Param("onlineStatus") Integer onlineStatus, @Param("businessId") Long businessId);

    /**
     * 根据账号查询
     *
     * @param account
     * @return
     */
    ClientAccount findByAccountName(@Param("account") String account);

    Map selectCurrentClientInfo(@Param("bid") Long bid);

    String selectIMEIByBid(@Param("bid") Long bid);

    int updateClientVideoSetting(@Param("businessId") Long businessId, @Param("diyFlag") Integer diyFlag, @Param("resolution") String resolution);
}