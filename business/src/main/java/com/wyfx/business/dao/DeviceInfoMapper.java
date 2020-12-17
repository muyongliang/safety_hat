package com.wyfx.business.dao;

import com.wyfx.business.entity.DeviceInfo;
import com.wyfx.business.entity.vo.DeviceInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DeviceInfoMapper {
    int deleteByPrimaryKey(Long did);

    int insert(DeviceInfo record);

    int insertSelective(DeviceInfo record);

    DeviceInfo selectByPrimaryKey(Long did);

    int updateByPrimaryKeySelective(DeviceInfo record);

    int updateByPrimaryKey(DeviceInfo record);

    DeviceInfo checkDeviceNumber(@Param("businessId") Integer businessId, @Param("number") String number);

    DeviceInfo checkDeviceIMEI(@Param("businessId") Integer businessId, @Param("imei") String imei);

    List<DeviceInfoVo> selectByBusinessId(@Param("businessId") Integer businessId);

    List<DeviceInfoVo> selectDevicesByStatus(@Param("businessId") Integer businessId, @Param("status") Integer status);

    List<DeviceInfoVo> selectDevicesByType(@Param("businessId") Integer businessId, @Param("type") Integer type);

    List<DeviceInfoVo> selectDevicesByTime(@Param("businessId") Integer businessId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    List<DeviceInfoVo> selectDevicesByNumber(@Param("businessId") Integer businessId, @Param("number") String number);

    List<DeviceInfoVo> selectDevicesByIMEI(@Param("businessId") Integer businessId, @Param("imei") String imei);

    List<DeviceInfoVo> selectDevicesByAccount(@Param("businessId") Integer businessId, @Param("account") String account);

    List<DeviceInfoVo> selectDevicesByName(@Param("businessId") Integer businessId, @Param("name") String name);

    /**
     * @param ids create by wsm on 2019-11-22
     * @return
     */
    List<DeviceInfo> selectByIds(List<Long> ids);

    List<DeviceInfoVo> selectByDids(@Param("businessId") Integer businessId, @Param("dids") List<Long> dids);

    DeviceInfo selectDetailByBid(@Param("bid") Long bid);

    Long selectIdByImei(@Param("imei") String imei);
}